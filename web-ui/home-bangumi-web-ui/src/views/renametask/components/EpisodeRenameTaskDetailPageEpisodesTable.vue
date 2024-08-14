<script setup lang="ts">
import { ref, Ref, onBeforeMount, h } from "vue";
import "plus-pro-components/es/components/form/style/css";
import { ElMessageBox } from "element-plus";
import { TaskItemColumns } from "./episodeRenameTaskDetailPageEpisodesTableColumns";
import {
  EpisodeRenameTaskStatusEnum,
  EpisodeRenameTaskItemStatusEnum
} from "../enums";
import EisodeRenameTaskItemManualParseDialogForm from "./EisodeRenameTaskItemManualParseDialogForm.vue";

import { ApiResult } from "@/api/base";
import {
  EpisodeRenameTaskItemsResp,
  getTaskItems,
  ignoreTaskItem
} from "@/api/renameTask";
import { message } from "@/utils/message";

defineOptions({
  name: "EpisodeRenameTaskDetailPageEpisodesTable"
});

// 按钮事件通知父组件
const emit = defineEmits<{
  /** 忽略解析 */
  (e: "ignore-success"): void;
  /** 手动解析成功 */
  (e: "manual-parse-success"): void;
}>();

const props = withDefaults(
  defineProps<{
    height?: string;
    taskId: number;
    key?: number;
  }>(),
  {
    height: null
  }
);

const tableListData = ref([]);
const taskStatus = ref(EpisodeRenameTaskStatusEnum.NONE);

const reqButtonDisable = ref(false);
const ignoreLoading = ref(false);
const deleteLoading = ref(false);

const beforeReqForButton = (buttonLoading: Ref<boolean>) => {
  buttonLoading.value = true;
  reqButtonDisable.value = true;
};

const afterReqForButton = (buttonLoading: Ref<boolean>) => {
  buttonLoading.value = false;
  reqButtonDisable.value = false;
};

// 初始化剧集信息
async function initTaskItems(id?: number) {
  const resp: EpisodeRenameTaskItemsResp = await getTaskItems(id);
  if (!resp.success) {
    return;
  }
  taskStatus.value = resp.data.taskStatus;
  tableListData.value = resp.data.items;
}

onBeforeMount(() => {
  initTaskItems(props.taskId);
});

// 处理忽略
function handleIgnore(row) {
  ElMessageBox({
    title: "忽略任务项",
    message: () =>
      h("div", null, [
        h("p", null, [h("span", null, "确认忽略当前任务项吗？忽略后不可恢复")])
      ]),
    showCancelButton: true,
    confirmButtonText: "确认",
    cancelButtonText: "取消"
  })
    .then(() => {
      doHandleIgnore(row.id);
    })
    .catch(() => {});
}

const doHandleIgnore = async (itemId: number) => {
  beforeReqForButton(ignoreLoading);
  const resp: ApiResult = await ignoreTaskItem(props.taskId, itemId);
  afterReqForButton(ignoreLoading);
  if (!resp.success) {
    return;
  }
  message("已忽略该任务项", { type: "success" });
  emit("ignore-success");
};

const manualParseFormVisible = ref(false);
const manualParseFormItemId = ref(-1);
const manualParseFormRenamedEpisodeFileName = ref("");
const manualParseFormEpisodeNo = ref(0);

// 处理手动解析
function handleManualParse(row) {
  manualParseFormItemId.value = row.id;
  manualParseFormRenamedEpisodeFileName.value = row.renamedEpisodeFileName
    ? row.renamedEpisodeFileName
    : "";
  manualParseFormEpisodeNo.value = row.episodeNo ? row.episodeNo : 0;
  openManualParseForm();
}

function openManualParseForm() {
  manualParseFormVisible.value = true;
}

function closeManualParseForm() {
  manualParseFormVisible.value = false;
}

// 手动解析成功
function onManualParseSuccess() {
  emit("manual-parse-success");
}
</script>
<template>
  <span :key="key" />
  <eisode-rename-task-item-manual-parse-dialog-form
    v-if="manualParseFormVisible"
    :task-id="props.taskId"
    :item-id="manualParseFormItemId"
    :renamed-episode-file-name="manualParseFormRenamedEpisodeFileName"
    :episode-no="manualParseFormEpisodeNo"
    @close-form="closeManualParseForm"
    @manual-parse-success="onManualParseSuccess"
  />

  <pure-table :data="tableListData" :columns="TaskItemColumns" :height="800">
    <template #operation="{ row }">
      <template v-if="EpisodeRenameTaskStatusEnum.NONE == taskStatus">
        <el-button
          v-if="
            EpisodeRenameTaskItemStatusEnum.PARSED == row.status ||
            EpisodeRenameTaskItemStatusEnum.TITLE_PARSE_FAILED == row.status
          "
          link
          type="primary"
          size="small"
          :disabled="reqButtonDisable"
          @click="handleManualParse(row)"
        >
          手动解析
        </el-button>
        <el-button
          v-if="
            EpisodeRenameTaskItemStatusEnum.IGNORED != row.status &&
            EpisodeRenameTaskItemStatusEnum.FILTERED_OUT != row.status
          "
          link
          type="primary"
          size="small"
          :loading="ignoreLoading"
          :disabled="reqButtonDisable"
          @click="handleIgnore(row)"
          >忽略</el-button
        >
      </template>
    </template>
  </pure-table>
</template>
