<script setup lang="ts">
import { ref, onBeforeMount, computed, h } from "vue";
import { Check } from "@element-plus/icons-vue";
import "plus-pro-components/es/components/form/style/css";
import {
  type PlusColumn,
  type FieldValues,
  PlusDialogForm
} from "plus-pro-components";

import {
  CreateEpisodeRenameTaskReq,
  CreateEpisodeRenameTaskResp,
  createRenameTask
} from "@/api/renameTask";

import {
  EpisodeRenameTaskSettingsResp,
  getEpisodeRenameTaskSettings
} from "@/api/systemSettings";

import {
  EpisodeTitleRenameMethodEnum,
  EpisodeTitleRenameMethodOptions
} from "../enums";

import { IsEmptyDirResp, checkIsEmptyDir } from "@/api/filePreView";

import { message } from "@/utils/message";

import { ElButton } from "element-plus";

import RemoteFileSelectDialogForm from "@/components/RemoteFileSelectDialogForm/index.vue";

defineOptions({
  name: "EpisodeRenameTaskCreateDialogForm"
});

const visible = ref(true);
const createLoading = ref(false);
const checkLoading = ref(false);

const globalSourceDirPath = ref("");
const globalOutDirPath = ref("");

// 关闭表单时通知父组件
const emit = defineEmits<{
  (e: "close-form"): void;
  (e: "create-success", id: number): void;
}>();

// 提交任务相关的表单
const taskState = ref<FieldValues>({
  taskName: "", // 任务鸣潮
  season: 1, // 剧集季度
  episodeDirPath: "", // 剧集目录路径
  episodeDirPathMaxDepth: 1, // 目录最大深度
  renamedOutputDirPath: "", // 重命名后输出的目录路径
  episodeTitleRenameMethod: EpisodeTitleRenameMethodEnum.TORRENT_PARSED_TITLE, // 剧集解析方式
  customizeRenamedEpisodeTitleFormat: "", // 自定义的重命名后标题格式
  filteredOutRules: [], // 过滤规则
  deleteSourceFile: false, // 是否删除源文件
  overwriteExistingFile: false // 覆盖已存在文件
});

/** 初始化任务的表单 */
function initTaskState() {
  taskState.value.taskName = "";
  taskState.value.season = 1;
  taskState.value.episodeDirPath = "";
  taskState.value.episodeDirPathMaxDepth = 1;
  taskState.value.renamedOutputDirPath = "";
  taskState.value.episodeTitleRenameMethod =
    EpisodeTitleRenameMethodEnum.TORRENT_PARSED_TITLE;
  taskState.value.customizeRenamedEpisodeTitleFormat = "";
  taskState.value.filteredOutRules = [];
  taskState.value.deleteSourceFile = false;
  taskState.value.overwriteExistingFile = false;
}

// 初始化路径配置
async function initPathSettings() {
  const resp: EpisodeRenameTaskSettingsResp =
    await getEpisodeRenameTaskSettings();
  if (!resp.success) {
    return;
  }

  globalSourceDirPath.value = resp.data.sourceDirPath ?? "";
  globalOutDirPath.value = resp.data.outDirPath ?? "";

  taskState.value.episodeDirPath = globalSourceDirPath.value;
  taskState.value.renamedOutputDirPath = globalOutDirPath.value;
}

function getLastPartOfPath(filePath: string) {
  const lastSlashIndex = Math.max(
    filePath.lastIndexOf("/"),
    filePath.lastIndexOf("\\")
  );

  if (lastSlashIndex !== -1) {
    return filePath.substring(lastSlashIndex + 1);
  }
  return filePath; // 如果路径中没有斜杠或反斜杠，直接返回整个路径
}

const autoGenerateRenamedOutputDirPath = () => {
  if (
    taskState.value.renamedOutputDirPath != globalOutDirPath.value ||
    taskState.value.renamedOutputDirPath == ""
  ) {
    return;
  }

  if (
    taskState.value.episodeDirPath == "" ||
    taskState.value.episodeDirPath == globalSourceDirPath.value
  ) {
    return;
  }

  const dirName = getLastPartOfPath(taskState.value.episodeDirPath as string);
  if (globalOutDirPath.value.lastIndexOf("/") > 0) {
    taskState.value.renamedOutputDirPath =
      globalOutDirPath.value + "/" + dirName;
  } else {
    taskState.value.renamedOutputDirPath =
      globalOutDirPath.value + "\\" + dirName;
  }
};

onBeforeMount(() => {
  initTaskState();
  initPathSettings();
});

// 任务表单相关规则
const taskRules = {
  taskName: [
    {
      required: true,
      message: "请输入任务名"
    }
  ],
  season: [
    {
      required: true,
      message: "请选择剧集季度"
    }
  ],
  episodeDirPath: [
    {
      required: true,
      message: "请输入剧集所在目录"
    }
  ],
  episodeDirPathMaxDepth: [
    {
      required: true,
      message: "请输入剧集所在目录的最大深度"
    }
  ],
  renamedOutputDirPath: [
    {
      required: true,
      message: "请输入重命名后输出目录"
    }
  ],
  episodeTitleRenameMethod: [
    {
      required: true,
      message: "请选择剧集重命名方式"
    }
  ]
};

// task表单的列
const taskColumns: PlusColumn[] = [
  {
    label: "任务名",
    width: 120,
    prop: "taskName",
    valueType: "copy",
    colProps: {
      span: 16
    }
  },
  {
    label: "剧集季度",
    width: 120,
    prop: "season",
    valueType: "input-number",
    colProps: {
      span: 8
    }
  },
  {
    label: "剧集目录路径",
    width: 120,
    prop: "episodeDirPath",
    tooltip: "如果使用容器部署，注意路径为容器中的路径",
    valueType: "copy",
    colProps: {
      span: 18
    },
    fieldProps: {
      onChange: autoGenerateRenamedOutputDirPath
    }
  },
  {
    label: "",
    renderLabel: () => {
      return "";
    },
    prop: "selectLoadEpisodeDirButton",
    renderField: () => {
      return h(
        ElButton,
        {
          onClick: handleClickSelectLoadEpisodeDirButton
        },
        () => "..."
      );
    },
    colProps: {
      span: 2
    }
  },
  {
    label: "目录最大深度",
    width: 120,
    prop: "episodeDirPathMaxDepth",
    valueType: "input-number",
    colProps: {
      span: 4
    }
  },
  {
    label: "重命名后输出的目录路径",
    width: 120,
    prop: "renamedOutputDirPath",
    valueType: "copy",
    tooltip: "如果使用容器部署，注意路径为容器中的路径",
    colProps: {
      span: 22
    }
  },
  {
    label: "",
    renderLabel: () => {
      return "";
    },
    prop: "checkRenamedOutputDirPathButton",
    renderField: () => {
      return h(ElButton, {
        onClick: handleClickCheckRenamedOutputDirPathButton,
        icon: Check,
        loading: checkLoading.value
      });
    },
    colProps: {
      span: 2
    }
  },
  {
    label: "剧集重命名方式",
    width: 120,
    prop: "episodeTitleRenameMethod",
    valueType: "select",
    options: EpisodeTitleRenameMethodOptions,
    colProps: {
      span: 8
    }
  },
  {
    label: "自定义剧集标题格式",
    width: 120,
    prop: "customizeRenamedEpisodeTitleFormat",
    valueType: "copy",
    fieldProps: computed(() => ({
      disabled:
        taskState.value.episodeTitleRenameMethod !==
        EpisodeTitleRenameMethodEnum.CUSTOMIZED_TITLE
    })),
    colProps: {
      span: 16
    },
    renderExtra: () => `支持的占位符: {season}, {episode}`
  },
  {
    label: "过滤规则",
    width: 120,
    prop: "filteredOutRules",
    renderExtra: () => `如果文件名包含对应规则，那么会过滤掉对应的数据`,
    valueType: "plus-input-tag",
    colProps: {
      span: 24
    }
  },
  {
    label: "删除源文件",
    width: 100,
    prop: "deleteSourceFile",
    valueType: "switch",
    colProps: {
      span: 24
    }
  },
  {
    label: "覆盖已存在文件",
    width: 100,
    prop: "overwriteExistingFile",
    valueType: "switch",
    colProps: {
      span: 24
    }
  }
];

const handleCreate = async (
  handleSubmit: () => Promise<boolean>,
  handleReset: () => void
) => {
  const isPass = await handleSubmit();
  if (!isPass) {
    return;
  }
  const req: CreateEpisodeRenameTaskReq = {
    taskName: taskState.value.taskName as string,
    season: taskState.value.season as number,
    episodeDirPath: taskState.value.episodeDirPath as string,
    episodeDirPathMaxDepth: taskState.value.episodeDirPathMaxDepth as number,
    renamedOutputDirPath: taskState.value.renamedOutputDirPath as string,
    episodeTitleRenameMethod: taskState.value
      .episodeTitleRenameMethod as number,
    customizeRenamedEpisodeTitleFormat: taskState.value
      .customizeRenamedEpisodeTitleFormat as string,
    filteredOutRules: taskState.value.filteredOutRules as Array<string>,
    deleteSourceFile: taskState.value.deleteSourceFile as boolean,
    overwriteExistingFile: taskState.value.overwriteExistingFile as boolean
  };
  createLoading.value = true;
  const resp: CreateEpisodeRenameTaskResp = await createRenameTask(req);
  createLoading.value = false;

  if (!resp.success) {
    return;
  }
  message("rss bangumi preview success", { type: "success" });
  handleCloseForm(handleReset);
  // 抛出预览成功事件
  emit("create-success", resp.data.taskId);
};

const handleCloseForm = async (handleReset: () => void) => {
  await handleReset();
  //initRssLinkState();
  visible.value = false;
  emit("close-form");
};

const selectLoadEpisodeDirFormVisible = ref(false);
const handleClickSelectLoadEpisodeDirButton = () => {
  selectLoadEpisodeDirFormVisible.value = true;
};

function closeSelectLoadEpisodeDirForm() {
  selectLoadEpisodeDirFormVisible.value = false;
}

function onSelectLoadEpisodeDirSuccess(path: string) {
  taskState.value.episodeDirPath = path;
  autoGenerateRenamedOutputDirPath();
}

const handleClickCheckRenamedOutputDirPathButton = async () => {
  checkLoading.value = true;
  const renamedOutputDirPath = taskState.value.renamedOutputDirPath as string;
  const resp: IsEmptyDirResp = await checkIsEmptyDir(renamedOutputDirPath);
  checkLoading.value = false;
  if (!resp.success) {
    return;
  }

  if (resp.data.isFile) {
    message(renamedOutputDirPath + " is file, please reinput", {
      type: "error"
    });
  } else if (resp.data.isEmpty) {
    message(renamedOutputDirPath + " is empty dir", {
      type: "success"
    });
  } else {
    message(renamedOutputDirPath + " is not empty dir", {
      type: "warning"
    });
  }
};
</script>
<template>
  <PlusDialogForm
    v-model:visible="visible"
    v-model="taskState"
    :dialog="{ title: '创建重命名任务', hasFooter: false }"
    :form="{
      columns: taskColumns,
      rules: taskRules,
      rowProps: { gutter: 20 },
      footerAlign: 'right',
      hasFooter: true,
      labelPosition: 'top'
    }"
  >
    <template #form-footer="{ handleSubmit, handleReset }">
      <el-button type="danger" @click="handleCloseForm(handleReset)">
        返回</el-button
      >
      <el-button
        type="primary"
        :loading="createLoading"
        @click="handleCreate(handleSubmit, handleReset)"
        >预览</el-button
      >
    </template>
  </PlusDialogForm>

  <remote-file-select-dialog-form
    v-if="selectLoadEpisodeDirFormVisible"
    :root-path="globalSourceDirPath"
    @closeForm="closeSelectLoadEpisodeDirForm"
    @select-success="onSelectLoadEpisodeDirSuccess"
  />
</template>
