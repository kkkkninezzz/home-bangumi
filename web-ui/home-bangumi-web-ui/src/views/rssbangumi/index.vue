<script setup lang="ts">
import RssBangumiCard from "./components/RssBangumiCard.vue";
import { useDetailPage } from "./components/hooks";
import { message } from "@/utils/message";
import { ElMessageBox, ElSwitch } from "element-plus";
import { ref, onMounted, h } from "vue";
import RssLinkCreateDialogForm from "./components/RssLinkCreateDialogForm.vue";
import RssBangumiSearchForm from "./components/RssBangumiSearchForm.vue";
import { useRenderIcon } from "@/components/ReIcon/src/hooks";
import AddFill from "@iconify-icons/ri/add-circle-line";

import { ApiResult } from "@/api/base";
import {
  LoadPagedRssbangumisResp,
  LoadPagedRssbangumisReq,
  loadPagedRssBangumis,
  PagedRssbBangumiItemDto,
  activeRssBangumi,
  inactiveRssBangumi,
  archiveRssBangumi,
  deleteRssBangumi
} from "@/api/rssBangumi";
import { RssBangumiCardPropType } from "./props";

defineOptions({
  name: "RssBangumi"
});

const { toDetailPage, router } = useDetailPage();
const svg = `
        <path class="path" d="
          M 30 15
          L 28 17
          M 25.61 25.61
          A 15 15, 0, 0, 1, 15 30
          A 15 15, 0, 1, 1, 27.99 7.5
          L 15 15
        " style="stroke-width: 4px; fill: rgba(0, 0, 0, 0)"/>
      `;

const pagination = ref({ current: 1, pageSize: 12, total: 1 });
const dataLoading = ref(false);
const bangumiList = ref(new Array<RssBangumiCardPropType>());
const searchConditions = ref({
  rssName: null,
  bangumiTitle: null,
  rssCategory: null,
  handleMethod: null,
  status: null
});

const getCardListData = async () => {
  const req: LoadPagedRssbangumisReq = {
    current: pagination.value.current,
    pageSize: pagination.value.pageSize,
    condition: {
      rssName: searchConditions.value.rssName,
      bangumiTitle: searchConditions.value.bangumiTitle,
      rssCategory: searchConditions.value.rssCategory,
      handleMethod: searchConditions.value.handleMethod,
      status: searchConditions.value.status
    }
  };
  dataLoading.value = true;
  const resp: LoadPagedRssbangumisResp = await loadPagedRssBangumis(req);
  dataLoading.value = false;
  if (!resp.success) {
    return;
  }

  const result: Array<RssBangumiCardPropType> = resp.data.list.map(
    (item, index, array) => {
      return toRssBangumiCardPropType(item);
    }
  );
  bangumiList.value = result;
  pagination.value.current = resp.data.current;
  pagination.value.pageSize = resp.data.pageSize;
  pagination.value.total = resp.data.total;
};

function toRssBangumiCardPropType(
  dto: PagedRssbBangumiItemDto
): RssBangumiCardPropType {
  return {
    id: dto.id,
    rssName: dto.rssName,
    rssCategory: dto.rssCategory,
    handleMethod: dto.handleMethod,
    status: dto.status,
    bangumiTitle: dto.bangumiInfo?.title ?? "",
    posterUrl: dto.bangumiInfo?.posterUrl ?? "",
    broadcastDayOfWeek: dto.bangumiInfo?.broadcastDayOfWeek ?? null,
    broadcastDate: dto.bangumiInfo?.broadcastDate ?? null,
    season: dto.bangumiInfo?.season ?? 0
  };
}

onMounted(() => {
  getCardListData();
});

const onPageSizeChange = (size: number) => {
  pagination.value.pageSize = size;
  pagination.value.current = 1;
  getCardListData();
};
const onCurrentChange = (current: number) => {
  pagination.value.current = current;
  getCardListData();
};

const handleManageRssBangumi = (id: number) => {
  toDetailPage({ id: id });
};

const handleInactiveRssBangumi = async (id: number) => {
  const resp: ApiResult = await inactiveRssBangumi(id);
  if (!resp.success) {
    return;
  }
  message("暂停订阅成功", { type: "success" });
  getCardListData();
};

const handleActiveRssBangumi = async (id: number) => {
  const resp: ApiResult = await activeRssBangumi(id);
  if (!resp.success) {
    return;
  }
  message("启用订阅成功", { type: "success" });
  getCardListData();
};

const handleDeleteRssBangumi = (id: number) => {
  const deleteFile = ref<boolean>(false);
  ElMessageBox({
    title: "删除rss番剧",
    message: () =>
      h("div", null, [
        h("p", null, [
          h(
            "span",
            null,
            "确定要删除吗？删除后不可恢复，同时会删除提交到下载器中的种子文件"
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
      doHandleDeleteRssBangumi(id, deleteFile.value);
    })
    .catch(() => {});
};

const doHandleDeleteRssBangumi = async (id: number, deleteFile: boolean) => {
  const resp: ApiResult = await deleteRssBangumi(id, deleteFile);
  if (!resp.success) {
    return;
  }
  message("删除成功", { type: "success" });
  getCardListData();
};

const handleArchiveRssBangumi = (id: number) => {
  const deleteFile = ref<boolean>(false);
  ElMessageBox({
    title: "归档rss番剧",
    message: () =>
      h("div", null, [
        h("p", null, [
          h(
            "span",
            null,
            "确定要归档吗？归档后不可恢复，同时会删除提交到下载器中的种子文件"
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
      doHandleArchive(id, deleteFile.value);
    })
    .catch(() => {});
};

const doHandleArchive = async (id: number, deleteFile: boolean) => {
  const resp: ApiResult = await archiveRssBangumi(id, deleteFile);
  if (!resp.success) {
    return;
  }
  message("归档成功", { type: "success" });
  getCardListData();
};

const createFormVisible = ref(false);
function openCreateForm() {
  createFormVisible.value = true;
}
function closeCreateForm() {
  createFormVisible.value = false;
}

// 当预览成功时
function onPreviewRssBangumiSuccess(id: number) {
  toDetailPage({ id: id });
}

function onSearchClick(
  rssNameOrBanugmiTitle: string,
  rssCategory: number,
  handleMethod: number,
  status: number
) {
  searchConditions.value.rssName = rssNameOrBanugmiTitle;
  searchConditions.value.bangumiTitle = rssNameOrBanugmiTitle;
  searchConditions.value.rssCategory = rssCategory;
  searchConditions.value.handleMethod = handleMethod;
  searchConditions.value.status = status;

  // 当点击搜索按钮时，页码重置
  pagination.value.current = 1;
  getCardListData();
}
</script>

<template>
  <div>
    <div class="w-full">
      <el-row :gutter="20">
        <el-col :span="4"
          ><el-button :icon="useRenderIcon(AddFill)" @click="openCreateForm">
            新增Rss番剧
          </el-button>

          <rss-link-create-dialog-form
            v-if="createFormVisible"
            @cancel="closeCreateForm"
            @closeForm="closeCreateForm"
            @preview-success="onPreviewRssBangumiSuccess"
          />
        </el-col>
        <el-col :span="20">
          <rss-bangumi-search-form
            :loading="dataLoading"
            @search="onSearchClick"
        /></el-col>
      </el-row>
    </div>
    <div
      v-loading="dataLoading"
      :element-loading-svg="svg"
      element-loading-svg-view-box="-10, -10, 50, 50"
    >
      <el-empty v-if="pagination.total <= 0" :description="`不存在rss番剧`" />
      <template v-else>
        <el-space wrap :size="30" style="width: 100%">
          <template v-for="(bangumi, index) in bangumiList" :key="index">
            <rss-bangumi-card
              :rssBangumiCard="bangumi"
              @delete-rss-bangumi="handleDeleteRssBangumi"
              @manage-rss-bangumi="handleManageRssBangumi"
              @inactive-rss-bangumi="handleInactiveRssBangumi"
              @active-rss-bangumi="handleActiveRssBangumi"
              @archive-rss-bangumi="handleArchiveRssBangumi"
            />
          </template>
        </el-space>
        <el-pagination
          v-model:currentPage="pagination.current"
          class="float-right"
          :page-size="pagination.pageSize"
          :total="pagination.total"
          :page-sizes="[12, 24, 36]"
          :background="true"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="onPageSizeChange"
          @current-change="onCurrentChange"
        />
      </template>
    </div>
  </div>
</template>
