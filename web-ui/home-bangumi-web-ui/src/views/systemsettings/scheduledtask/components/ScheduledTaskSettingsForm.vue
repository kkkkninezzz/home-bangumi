<script setup lang="ts">
import { ref, Ref, onBeforeMount, h } from "vue";
import "plus-pro-components/es/components/form/style/css";
import {
  type PlusColumn,
  type FieldValues,
  PlusForm
} from "plus-pro-components";

import {
  ScheduledTaskSettingsResp,
  UpdateScheduledTaskSettingsReq,
  getScheduledTaskSettings,
  updateScheduledTaskSettings
} from "@/api/systemSettings";

import { RunOnceScheduledTaskReq, runOnceTask } from "@/api/scheduledTask";
import { ApiResult } from "@/api/base";

import { ScheduledTaskIdEnum } from "../enums";

import { message } from "@/utils/message";
import { ElButton, ElMessageBox } from "element-plus";

defineOptions({
  // name 作为一种规范最好必须写上并且和路由的name保持一致
  name: "ScheduledTaskSettingsForm"
});

// 配置的表单状态
const settingsState = ref<FieldValues>({
  checkEpisodeDownloadStatusDuration: 1,
  pushParsedEpisodesToDownloaderDuration: 1,
  renameEpisodesDuration: 1,
  updateRssSubscriptionDuration: 1
});

// 初始化设置的表单
async function initSettingsForm() {
  const resp: ScheduledTaskSettingsResp = await getScheduledTaskSettings();
  if (!resp.success) {
    return;
  }
  setSettingsState(resp);
}

function setSettingsState(resp: ScheduledTaskSettingsResp) {
  settingsState.value.checkEpisodeDownloadStatusDuration =
    resp.data.checkEpisodeDownloadStatusDuration;
  settingsState.value.pushParsedEpisodesToDownloaderDuration =
    resp.data.pushParsedEpisodesToDownloaderDuration;
  settingsState.value.renameEpisodesDuration = resp.data.renameEpisodesDuration;
  settingsState.value.updateRssSubscriptionDuration =
    resp.data.updateRssSubscriptionDuration;
}

onBeforeMount(() => {
  initSettingsForm();
});

// 相关规则
const settingsRules = {
  checkEpisodeDownloadStatusDuration: [
    {
      required: true,
      message: "请输入检查番剧下载状态的定时任务周期"
    }
  ],
  pushParsedEpisodesToDownloaderDuration: [
    {
      required: true,
      message: "请输入推送已经解析好的番剧到下载器的定时任务周期"
    }
  ],
  renameEpisodesDuration: [
    {
      required: true,
      message: "重命名剧集的定时任务周期"
    }
  ],
  updateRssSubscriptionDuration: [
    {
      required: true,
      message: "请输入更新rss bangumi的定时任务周期"
    }
  ]
};

const settingsColumns: PlusColumn[] = [
  {
    label: "检查番剧下载状态(分钟)",
    //width: 120,
    prop: "checkEpisodeDownloadStatusDuration",
    valueType: "input-number",
    fieldProps: { min: 0, clearable: false },
    colProps: {
      span: 20
    }
  },
  {
    label: "",
    renderLabel: () => {
      return "";
    },
    prop: "checkEpisodeDownloadStatusDurationButton",
    renderField: () => {
      return h(
        ElButton,
        {
          type: "primary",
          onClick: handleClickCheckEpisodeDownloadStatusDurationButton
        },
        () => "立即执行"
      );
    },
    colProps: {
      span: 4
    }
  },
  {
    label: "推送解析结束的番剧到下载器(分钟)",
    //width: 120,
    prop: "pushParsedEpisodesToDownloaderDuration",
    valueType: "input-number",
    fieldProps: { min: 0, clearable: false },
    colProps: {
      span: 20
    }
  },
  {
    label: "",
    renderLabel: () => {
      return "";
    },
    prop: "pushParsedEpisodesToDownloaderDurationButton",
    renderField: () => {
      return h(
        ElButton,
        {
          type: "primary",
          onClick: handleClickPushParsedEpisodesToDownloaderDurationButton
        },
        () => "立即执行"
      );
    },
    colProps: {
      span: 4
    }
  },
  {
    label: "重命名剧集(分钟)",
    //width: 120,
    prop: "renameEpisodesDuration",
    valueType: "input-number",
    fieldProps: { min: 0, clearable: false },
    colProps: {
      span: 20
    }
  },
  {
    label: "",
    renderLabel: () => {
      return "";
    },
    prop: "renameEpisodesDurationButton",
    renderField: () => {
      return h(
        ElButton,
        {
          type: "primary",
          onClick: handleClickRenameEpisodesDurationButton
        },
        () => "立即执行"
      );
    },
    colProps: {
      span: 4
    }
  },
  {
    label: "自动更新rss番剧(分钟)",
    //width: 120,
    prop: "updateRssSubscriptionDuration",
    valueType: "input-number",
    fieldProps: { min: 0, clearable: false },
    colProps: {
      span: 20
    }
  },
  {
    label: "",
    renderLabel: () => {
      return "";
    },
    prop: "updateRssSubscriptionDurationButton",
    renderField: () => {
      return h(
        ElButton,
        {
          type: "primary",
          onClick: handleClickUpdateRssSubscriptionDurationButton
        },
        () => "立即执行"
      );
    },
    colProps: {
      span: 4
    }
  }
];

const handleSubmit = async (values: FieldValues) => {
  const req: UpdateScheduledTaskSettingsReq = {
    checkEpisodeDownloadStatusDuration: settingsState.value
      .checkEpisodeDownloadStatusDuration as number,
    pushParsedEpisodesToDownloaderDuration: settingsState.value
      .pushParsedEpisodesToDownloaderDuration as number,
    renameEpisodesDuration: settingsState.value
      .renameEpisodesDuration as number,
    updateRssSubscriptionDuration: settingsState.value
      .updateRssSubscriptionDuration as number
  };

  const resp: ScheduledTaskSettingsResp =
    await updateScheduledTaskSettings(req);

  if (!resp.success) {
    return;
  }
  message("保存成功", { type: "success" });
  setSettingsState(resp);
};

const handleClickCheckEpisodeDownloadStatusDurationButton = () => {
  ElMessageBox.confirm("确认立即检查所有番剧的下载状态吗？", "立即执行", {
    type: "warning",
    center: true
  })
    .then(() => {
      doRunOnceScheduledTask(ScheduledTaskIdEnum.CHECK_EPISODE_DOWNLOAD);
    })
    .catch(() => {});
};

const handleClickPushParsedEpisodesToDownloaderDurationButton = () => {
  ElMessageBox.confirm(
    "确认立即推送所有解析好的番剧到下载器中吗？",
    "立即执行",
    {
      type: "warning",
      center: true
    }
  )
    .then(() => {
      doRunOnceScheduledTask(
        ScheduledTaskIdEnum.PUSH_PARSED_EPISODES_TO_DOWNLOADER
      );
    })
    .catch(() => {});
};

const handleClickRenameEpisodesDurationButton = () => {
  ElMessageBox.confirm("确认立即重命名所有下载结束的剧集吗？", "立即执行", {
    type: "warning",
    center: true
  })
    .then(() => {
      doRunOnceScheduledTask(ScheduledTaskIdEnum.RENAME_EPISODES);
    })
    .catch(() => {});
};

const handleClickUpdateRssSubscriptionDurationButton = () => {
  ElMessageBox.confirm("确认立即更新所有剧集的rss订阅状态吗？", "立即执行", {
    type: "warning",
    center: true
  })
    .then(() => {
      doRunOnceScheduledTask(ScheduledTaskIdEnum.UPDATE_RSS_SUBSCRIPTION);
    })
    .catch(() => {});
};

const doRunOnceScheduledTask = async (taskId: ScheduledTaskIdEnum) => {
  const req: RunOnceScheduledTaskReq = {
    taskId: taskId
  };
  const resp: ApiResult = await runOnceTask(req);

  if (!resp.success) {
    return;
  }
  message("执行成功", { type: "success" });
};
</script>

<template>
  <PlusForm
    label-position="top"
    v-model="settingsState"
    :rules="settingsRules"
    :columns="settingsColumns"
    :row-props="{ gutter: 20 }"
    footerAlign="right"
    @submit="handleSubmit"
  >
  </PlusForm>
</template>
