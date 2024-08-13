<script setup lang="ts">
import { ref, onBeforeMount, computed } from "vue";
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
  EpisodeTitleRenameMethodEnum,
  EpisodeTitleRenameMethodOptions
} from "../enums";

import { message } from "@/utils/message";

defineOptions({
  name: "EpisodeRenameTaskCreateDialogForm"
});

const visible = ref(true);
const createLoading = ref(false);

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
  taskState.value.renamedOutputDirPath = "";
  taskState.value.episodeTitleRenameMethod =
    EpisodeTitleRenameMethodEnum.TORRENT_PARSED_TITLE;
  taskState.value.customizeRenamedEpisodeTitleFormat = "";
  taskState.value.filteredOutRules = [];
  taskState.value.deleteSourceFile = false;
  taskState.value.overwriteExistingFile = false;
}

onBeforeMount(() => {
  initTaskState();
});

// rss链接相关规则
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

// rss链接表单的列
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
      span: 24
    }
  },
  {
    label: "重命名后输出的目录路径",
    width: 120,
    prop: "renamedOutputDirPath",
    valueType: "copy",
    tooltip: "如果使用容器部署，注意路径为容器中的路径",
    colProps: {
      span: 24
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
</template>
