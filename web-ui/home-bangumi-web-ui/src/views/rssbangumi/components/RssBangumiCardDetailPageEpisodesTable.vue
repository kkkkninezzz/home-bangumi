<script setup lang="ts">
import { ref, Ref, onBeforeMount, h } from "vue";
import "plus-pro-components/es/components/form/style/css";
import { ElMessageBox, ElSwitch } from "element-plus";
import { EpisodeColumns } from "./rssBangumiCardDetailPageEpisodesTableColumns";
import { RssBangumiEpisodeStatusEnum } from "../enums";
import RssBangumiEpisodeManualParseDialogForm from "./RssBangumiEpisodeManualParseDialogForm.vue";
import RssBangumiEpisodeRenameDialogForm from "./RssBangumiEpisodeRenameDialogForm.vue";

import {
  RssBangumRssCategoryOptions,
  RssBangumiHandleMethodOptions
} from "../enums";

import { ApiResult } from "@/api/base";
import {
  RssBangumiEpisodesResp,
  getRssBangumiEpisodes,
  ignoreEpisode,
  deleteEpisode
} from "@/api/rssBangumi";
import { message } from "@/utils/message";

defineOptions({
  name: "RssBangumiCardDetailPageEpisodesTable"
});

// 按钮事件通知父组件
const emit = defineEmits<{
  /** 重命名成功 */
  (e: "rename-success"): void;
  /** 忽略解析 */
  (e: "ignore-success"): void;
  /** 删除成功 */
  (e: "delete-success"): void;
  /** 手动解析成功 */
  (e: "manual-parse-success"): void;
}>();

const props = withDefaults(
  defineProps<{
    height?: string;
    rssBangumiId: number;
    key?: number;
  }>(),
  {
    height: null
  }
);

const tableListData = ref([]);

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
async function initRssBangumiEpisodes(id?: number) {
  const resp: RssBangumiEpisodesResp = await getRssBangumiEpisodes(id);
  if (!resp.success) {
    return;
  }
  tableListData.value = resp.data.episodes;
}

onBeforeMount(() => {
  initRssBangumiEpisodes(props.rssBangumiId);
});

// 处理忽略
function handleIgnore(row) {
  const deleteFile = ref<boolean>(true);
  ElMessageBox({
    title: "忽略剧集",
    message: () =>
      h("div", null, [
        h("p", null, [
          h(
            "span",
            null,
            "确认忽略当前剧集吗？忽略后不可恢复，提交到下载器的种子将删除"
          )
        ]),
        h("p", null, [
          h(ElSwitch, {
            modelValue: deleteFile.value,
            "onUpdate:modelValue": (val: boolean) => {
              deleteFile.value = val;
            },
            activeText: "删除下载完成的剧集文件"
          })
        ])
      ]),
    showCancelButton: true,
    confirmButtonText: "确认",
    cancelButtonText: "取消"
  })
    .then(() => {
      doHandleIgnore(row.id, deleteFile.value);
    })
    .catch(() => {});
}

const doHandleIgnore = async (episodeId: number, deleteFile: boolean) => {
  beforeReqForButton(ignoreLoading);
  const resp: ApiResult = await ignoreEpisode(
    props.rssBangumiId,
    episodeId,
    deleteFile
  );
  afterReqForButton(ignoreLoading);
  if (!resp.success) {
    return;
  }
  message("已忽略该剧集", { type: "success" });
  emit("ignore-success");
};

// 处理删除
function handleDelete(row) {
  const deleteFile = ref<boolean>(true);
  ElMessageBox({
    title: "删除剧集",
    message: () =>
      h("div", null, [
        h("p", null, [
          h(
            "span",
            null,
            "确认删除当前剧集吗？提交到下载器的种子将删除，如果进行增量解析，被删除的剧集将会重新进行解析"
          )
        ]),
        h("p", null, [
          h(ElSwitch, {
            modelValue: deleteFile.value,
            "onUpdate:modelValue": (val: boolean) => {
              deleteFile.value = val;
            },
            activeText: "删除下载完成的剧集文件"
          })
        ])
      ]),
    showCancelButton: true,
    confirmButtonText: "确认",
    cancelButtonText: "取消"
  })
    .then(() => {
      doHandleDelete(row.id, deleteFile.value);
    })
    .catch(() => {});
}

const doHandleDelete = async (episodeId: number, deleteFile: boolean) => {
  beforeReqForButton(deleteLoading);
  const resp: ApiResult = await deleteEpisode(
    props.rssBangumiId,
    episodeId,
    deleteFile
  );
  afterReqForButton(deleteLoading);
  if (!resp.success) {
    return;
  }
  message("已删除该剧集", { type: "success" });
  emit("delete-success");
};

const manualParseFormVisible = ref(false);
const manualParseFormEpisodeId = ref(-1);
const manualParseFormRenamedEpisodeFileName = ref("");
const manualParseFormEpisodeNo = ref(0);

// 处理手动解析
function handleManualParse(row) {
  manualParseFormEpisodeId.value = row.id;
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

const renameFormVisible = ref(false);
const renameFormEpisodeId = ref(-1);
const renameFormRenamedEpisodeFileName = ref("");

// 处理重命名
function handleRename(row) {
  renameFormEpisodeId.value = row.id;
  renameFormRenamedEpisodeFileName.value = row.renamedEpisodeFileName
    ? row.renamedEpisodeFileName
    : "";
  openRenameForm();
}

function openRenameForm() {
  renameFormVisible.value = true;
}

function closeRenameForm() {
  renameFormVisible.value = false;
}

// 重命名成功
function onRenameSuccess() {
  emit("rename-success");
}
</script>
<template>
  <span :key="key" />
  <rss-bangumi-episode-manual-parse-dialog-form
    v-if="manualParseFormVisible"
    :rssBangumiId="props.rssBangumiId"
    :episodeId="manualParseFormEpisodeId"
    :renamedEpisodeFileName="manualParseFormRenamedEpisodeFileName"
    :episodeNo="manualParseFormEpisodeNo"
    @closeForm="closeManualParseForm"
    @manual-parse-success="onManualParseSuccess"
  />
  <rss-bangumi-episode-rename-dialog-form
    v-if="renameFormVisible"
    :rssBangumiId="props.rssBangumiId"
    :episodeId="renameFormEpisodeId"
    :renamedEpisodeFileName="renameFormRenamedEpisodeFileName"
    @closeForm="closeRenameForm"
    @rename-success="onRenameSuccess"
  />
  <pure-table :data="tableListData" :columns="EpisodeColumns" :height="800">
    <template #operation="{ row }">
      <template v-if="RssBangumiEpisodeStatusEnum.ARCHIVED != row.status">
        <el-button
          v-if="
            RssBangumiEpisodeStatusEnum.PARSED == row.status ||
            RssBangumiEpisodeStatusEnum.TITLE_PARSE_FAILED == row.status ||
            RssBangumiEpisodeStatusEnum.RENAME_FAILED == row.status
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
            RssBangumiEpisodeStatusEnum.FINISHED == row.status ||
            RssBangumiEpisodeStatusEnum.EPISODE_DOWNLOADING == row.status
          "
          link
          type="primary"
          size="small"
          :disabled="reqButtonDisable"
          @click="handleRename(row)"
        >
          重命名
        </el-button>
        <el-button
          v-if="
            RssBangumiEpisodeStatusEnum.IGNORED != row.status &&
            RssBangumiEpisodeStatusEnum.FILTERED_OUT != row.status
          "
          link
          type="primary"
          size="small"
          :loading="ignoreLoading"
          :disabled="reqButtonDisable"
          @click="handleIgnore(row)"
          >忽略</el-button
        >
        <el-button
          link
          type="primary"
          size="small"
          :loading="deleteLoading"
          :disabled="reqButtonDisable"
          @click="handleDelete(row)"
          >删除</el-button
        >
      </template>
    </template>
  </pure-table>
</template>
