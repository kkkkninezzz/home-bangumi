<script setup lang="ts">
import { ref, onMounted } from "vue";
import "plus-pro-components/es/components/form/style/css";
import {
  type PlusColumn,
  type FieldValues,
  PlusDialogForm
} from "plus-pro-components";

import { ApiResult } from "@/api/base";
import {
  RenameEpisodeFileNameReq,
  renameEpisodeFileName
} from "@/api/rssBangumi";
import { message } from "@/utils/message";

defineOptions({
  name: "RssBangumiEpisodeRenameDialogForm"
});

const props = withDefaults(
  defineProps<{
    rssBangumiId: number;
    episodeId: number;
    renamedEpisodeFileName: string;
  }>(),
  {}
);

const visible = ref(true);
const renameLoading = ref(false);

// 关闭表单时通知父组件
const emit = defineEmits<{
  (e: "close-form"): void;
  (e: "rename-success"): void;
}>();

// 重命名相关的表单
const renameState = ref<FieldValues>({
  newEpisodeFileName: ""
});

onMounted(() => {
  initRenameForm();
});

/** 初始化重命名的表单 */
async function initRenameForm() {
  renameState.value.newEpisodeFileName = props.renamedEpisodeFileName;
}

// 表单相关规则
const renameRules = {
  newEpisodeFileName: [
    {
      required: true,
      message: "请输入新的文件名"
    }
  ]
};

// 重命名表单的列
const manaulParseColumns: PlusColumn[] = [
  {
    label: "新的文件名",
    width: 120,
    prop: "newEpisodeFileName",
    valueType: "copy",
    colProps: {
      span: 24
    }
  }
];

const handleRename = async (
  handleSubmit: () => Promise<boolean>,
  handleReset: () => void
) => {
  const isPass = await handleSubmit();
  if (!isPass) {
    return;
  }
  const req: RenameEpisodeFileNameReq = {
    newFileName: renameState.value.newEpisodeFileName as string
  };
  renameLoading.value = true;
  const resp: ApiResult = await renameEpisodeFileName(
    props.rssBangumiId,
    props.episodeId,
    req
  );
  renameLoading.value = false;

  if (!resp.success) {
    return;
  }
  message("重命名成功", { type: "success" });
  handleCloseForm(handleReset);
  // 抛出重命名成功事件
  emit("rename-success");
};

const handleCloseForm = async (handleReset: () => void) => {
  emit("close-form");
};
</script>
<template>
  <PlusDialogForm
    v-model:visible="visible"
    v-model="renameState"
    :dialog="{ title: '重命名剧集信息', hasFooter: false }"
    :form="{
      columns: manaulParseColumns,
      rules: renameRules,
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
        :loading="renameLoading"
        @click="handleRename(handleSubmit, handleReset)"
        >提交</el-button
      >
    </template>
  </PlusDialogForm>
</template>
