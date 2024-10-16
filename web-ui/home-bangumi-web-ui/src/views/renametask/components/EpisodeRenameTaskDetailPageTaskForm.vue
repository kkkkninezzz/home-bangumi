<script setup lang="ts">
import { ref, Ref, onBeforeMount, computed, h } from "vue";
import { Check } from "@element-plus/icons-vue";
import { ElMessageBox } from "element-plus";
import "plus-pro-components/es/components/form/style/css";
import {
  type PlusColumn,
  type FieldValues,
  PlusForm
} from "plus-pro-components";

import {
  EpisodeRenameTaskStatusEnum,
  EpisodeTitleRenameMethodEnum,
  EpisodeTitleRenameMethodOptions
} from "../enums";

import { ApiResult } from "@/api/base";

import {
  EpisodeRenameTaskDetailResp,
  getTaskDetail,
  updateRssBangumi,
  UpdateEpisodeRenameTaskReq,
  submitTask,
  reparseTaskItems
} from "@/api/renameTask";

import {
  EpisodeRenameTaskSettingsResp,
  getEpisodeRenameTaskSettings
} from "@/api/systemSettings";

import { IsEmptyDirResp, checkIsEmptyDir } from "@/api/filePreView";

import { message } from "@/utils/message";
import { ElButton } from "element-plus";
import RemoteFileSelectDialogForm from "@/components/RemoteFileSelectDialogForm/index.vue";

defineOptions({
  name: "EpisodeRenameTaskDetailPageTaskForm"
});

const props = withDefaults(
  defineProps<{
    taskId: number;
    key?: number;
  }>(),
  {}
);

// 按钮事件通知父组件
const emit = defineEmits<{
  /** 重新解析成功 */
  (e: "reparse-success"): void;
  /** 提交任务成功 */
  (e: "submit-success"): void;
}>();

const updateLoading = ref(false);
const reparseLoading = ref(false);
const submitTaskLoading = ref(false);
const reqButtonDisable = ref(false);
const checkLoading = ref(false);

// 提交任务相关的表单
const taskState = ref<FieldValues>({
  id: null, // id
  taskStatus: EpisodeRenameTaskStatusEnum.NONE, // 任务状态
  taskName: "", // 任务名称
  season: 1, // 剧集季度
  episodeDirPath: "", // 剧集目录路径
  episodeDirPathMaxDepth: 1, // 目录最大深度
  renamedOutputDirPath: "", // 重命名后输出的目录路径
  episodeTitleRenameMethod: EpisodeTitleRenameMethodEnum.TORRENT_PARSED_TITLE, // 剧集解析方式
  customizeRenamedEpisodeTitleFormat: "", // 自定义的重命名后标题格式
  filteredOutRules: [], // 过滤规则
  deleteSourceFile: false, // 是否删除源文件
  overwriteExistingFile: false, // 覆盖已存在文件
  createdTime: 0 // 创建时间
});

// 初始化任务表单
async function initTaskForm(id?: number) {
  const resp: EpisodeRenameTaskDetailResp = await getTaskDetail(id);
  if (!resp.success) {
    return;
  }
  setTaskState(resp);
}

// 初始化rss链接的表单状态对象
function setTaskState(resp: EpisodeRenameTaskDetailResp) {
  taskState.value.id = resp.data.id;
  taskState.value.taskName = resp.data.taskName;
  taskState.value.season = resp.data.season;
  taskState.value.taskStatus = resp.data.taskStatus;
  taskState.value.episodeDirPath = resp.data.episodeDirPath;
  taskState.value.episodeDirPathMaxDepth = resp.data.episodeDirPathMaxDepth;
  taskState.value.renamedOutputDirPath = resp.data.renamedOutputDirPath;
  taskState.value.episodeTitleRenameMethod = resp.data.episodeTitleRenameMethod;
  taskState.value.customizeRenamedEpisodeTitleFormat =
    resp.data.customizeRenamedEpisodeTitleFormat;
  taskState.value.filteredOutRules = resp.data.filteredOutRules;
  taskState.value.deleteSourceFile = resp.data.deleteSourceFile;
  taskState.value.overwriteExistingFile = resp.data.overwriteExistingFile;
  taskState.value.createdTime = resp.data.createdTime;
}

const globalSourceDirPath = ref("");
const globalOutDirPath = ref("");
// 获取全局路径配置
async function getGloablPathSettings() {
  const resp: EpisodeRenameTaskSettingsResp =
    await getEpisodeRenameTaskSettings();
  if (!resp.success) {
    return;
  }

  globalSourceDirPath.value = resp.data.sourceDirPath ?? "";
  globalOutDirPath.value = resp.data.outDirPath ?? "";
}

onBeforeMount(() => {
  initTaskForm(props.taskId);
  getGloablPathSettings();
});

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
      span: 14
    }
  },
  {
    label: "剧集季度",
    width: 120,
    prop: "season",
    valueType: "input-number",
    colProps: {
      span: 4
    }
  },
  {
    label: "创建时间",
    prop: "createdTime",
    valueType: "date-picker",
    fieldProps: { disabled: true, type: "datetime" },
    colProps: {
      span: 6
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
    tooltip: "支持的占位符: {season}, {episode}",
    fieldProps: computed(() => ({
      disabled:
        taskState.value.episodeTitleRenameMethod !==
        EpisodeTitleRenameMethodEnum.CUSTOMIZED_TITLE
    })),
    colProps: {
      span: 16
    }
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

const beforeReqForButton = (buttonLoading: Ref<boolean>) => {
  buttonLoading.value = true;
  reqButtonDisable.value = true;
};

const afterReqForButton = (buttonLoading: Ref<boolean>) => {
  buttonLoading.value = false;
  reqButtonDisable.value = false;
};

const handleUpdateTask = async (values: FieldValues) => {
  const req: UpdateEpisodeRenameTaskReq = {
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

  beforeReqForButton(updateLoading);
  const resp: EpisodeRenameTaskDetailResp = await updateRssBangumi(
    props.taskId,
    req
  );
  afterReqForButton(updateLoading);
  if (!resp.success) {
    return;
  }
  message("保存成功", { type: "success" });
  setTaskState(resp);
};

// 处理提交任务
const handleSubmitTask = async () => {
  ElMessageBox.confirm("确认提交任务吗？提交后，任务不可再进行修改", "提交", {
    type: "warning",
    center: true
  })
    .then(() => {
      doHandleSubmitTask();
    })
    .catch(() => {});
};

const doHandleSubmitTask = async () => {
  beforeReqForButton(submitTaskLoading);
  const resp: ApiResult = await submitTask(props.taskId);
  afterReqForButton(submitTaskLoading);
  if (!resp.success) {
    return;
  }
  message("提交成功", { type: "success" });
  emit("submit-success");
};

// 处理重新解析
const handleReparse = async () => {
  ElMessageBox.confirm(
    "确认重新解析全部任务项吗？重新解析将删除目前所有的任务项",
    "重新解析",
    {
      type: "warning",
      center: true
    }
  )
    .then(() => {
      doHandleReparse();
    })
    .catch(() => {});
};

const doHandleReparse = async () => {
  beforeReqForButton(reparseLoading);
  const resp: ApiResult = await reparseTaskItems(props.taskId);
  afterReqForButton(reparseLoading);
  if (!resp.success) {
    return;
  }
  message("重新解析成功", { type: "success" });
  emit("reparse-success");
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
  <span :key="key" />
  <PlusForm
    v-model="taskState"
    :disabled="EpisodeRenameTaskStatusEnum.NONE != taskState.taskStatus"
    label-position="top"
    :columns="taskColumns"
    :rules="taskRules"
    :row-props="{ gutter: 20 }"
    footerAlign="right"
    @submit="handleUpdateTask"
  >
    <template #footer="{ handleSubmit }">
      <div style="margin: 0 auto">
        <el-button
          v-if="EpisodeRenameTaskStatusEnum.NONE == taskState.taskStatus"
          type="danger"
          :loading="submitTaskLoading"
          :disabled="reqButtonDisable"
          @click="handleSubmitTask"
          >提交任务</el-button
        >
        <el-button
          v-if="EpisodeRenameTaskStatusEnum.NONE == taskState.taskStatus"
          type="danger"
          :loading="reparseLoading"
          :disabled="reqButtonDisable"
          @click="handleReparse"
          >重新解析</el-button
        >

        <el-button
          v-if="EpisodeRenameTaskStatusEnum.NONE == taskState.taskStatus"
          type="primary"
          :loading="updateLoading"
          :disabled="reqButtonDisable"
          @click="handleSubmit"
          >保存</el-button
        >
      </div>
    </template>
  </PlusForm>

  <remote-file-select-dialog-form
    v-if="selectLoadEpisodeDirFormVisible"
    :root-path="globalSourceDirPath"
    @closeForm="closeSelectLoadEpisodeDirForm"
    @select-success="onSelectLoadEpisodeDirSuccess"
  />
</template>
