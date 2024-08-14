<script setup lang="ts">
import { computed, ref, onMounted } from "vue";
import "plus-pro-components/es/components/form/style/css";
import {
  type PlusColumn,
  type FieldValues,
  PlusDialogForm
} from "plus-pro-components";

import { ApiResult } from "@/api/base";
import {
  manualParseTaskItem,
  ManualParseRenameTaskItemReq
} from "@/api/renameTask";
import { message } from "@/utils/message";

defineOptions({
  name: "EisodeRenameTaskItemManualParseDialogForm"
});

const props = withDefaults(
  defineProps<{
    taskId: number;
    itemId: number;
    renamedEpisodeFileName: string;
    episodeNo: number;
  }>(),
  {}
);

const visible = ref(true);
const manualParseLoading = ref(false);

// 关闭表单时通知父组件
const emit = defineEmits<{
  (e: "close-form"): void;
  (e: "manual-parse-success"): void;
}>();

// 手动解析相关的表单
const manualParseState = ref<FieldValues>({
  episodeNo: 0, // 剧集号
  renamedEpisodeFileName: "" //重命名后的剧集文件名
});

onMounted(() => {
  initManualParseForm();
});

/** 初始化手动解析的表单 */
async function initManualParseForm() {
  manualParseState.value.episodeNo = props.episodeNo;
  if (props.renamedEpisodeFileName) {
    manualParseState.value.renamedEpisodeFileName =
      props.renamedEpisodeFileName;
  }
}

function getFileExtension(filename: string) {
  return filename.slice(((filename.lastIndexOf(".") - 1) >>> 0) + 2);
}

// 手动解析相关规则
const manualParseRules = {
  episodeNo: [
    {
      required: true,
      message: "请输入剧集号"
    }
  ],
  renamedEpisodeFileName: [
    {
      required: true,
      message: "请输入重命名后的剧集文件名"
    }
  ]
};

// 表单列
const manaulParseColumns: PlusColumn[] = [
  {
    label: "剧集号",
    width: 120,
    prop: "episodeNo",
    valueType: "input-number",
    fieldProps: { min: -1 },
    colProps: {
      span: 6
    }
  },
  {
    label: "重命名后的剧集文件名",
    width: 120,
    prop: "renamedEpisodeFileName",
    valueType: "copy",
    colProps: {
      span: 18
    }
  }
];

const handleManaulParse = async (
  handleSubmit: () => Promise<boolean>,
  handleReset: () => void
) => {
  const isPass = await handleSubmit();
  if (!isPass) {
    return;
  }
  const req: ManualParseRenameTaskItemReq = {
    episodeNo: manualParseState.value.episodeNo as number,
    renamedEpisodeFileName: manualParseState.value
      .renamedEpisodeFileName as string
  };
  manualParseLoading.value = true;
  const resp: ApiResult = await manualParseTaskItem(
    props.taskId,
    props.itemId,
    req
  );
  manualParseLoading.value = false;

  if (!resp.success) {
    return;
  }
  message("手动解析成功", { type: "success" });
  handleCloseForm(handleReset);
  // 抛出手动解析成功事件
  emit("manual-parse-success");
};

const handleCloseForm = async (handleReset: () => void) => {
  emit("close-form");
};
</script>
<template>
  <PlusDialogForm
    v-model:visible="visible"
    v-model="manualParseState"
    :dialog="{ title: '手动解析剧集信息', hasFooter: false }"
    :form="{
      columns: manaulParseColumns,
      rules: manualParseRules,
      rowProps: { gutter: 20 },
      footerAlign: 'right',
      hasFooter: true,
      labelPosition: 'top'
    }"
  >
    <template #form-footer="{ handleSubmit, handleReset }">
      <el-button type="danger" @click="handleCloseForm(handleReset)">
        取消</el-button
      >
      <el-button
        type="primary"
        :loading="manualParseLoading"
        @click="handleManaulParse(handleSubmit, handleReset)"
        >提交</el-button
      >
    </template>
  </PlusDialogForm>
</template>
