<script setup lang="ts">
import EpisodeRenameTaskCard from "./components/EpisodeRenameTaskCard.vue";
import { useDetailPage } from "./components/hooks";
import { message } from "@/utils/message";
import { ElMessageBox, ElSwitch } from "element-plus";
import { ref, onMounted, h } from "vue";
//import RssLinkCreateDialogForm from "./components/RssLinkCreateDialogForm.vue";
import EpisodeRenameTaskSearchForm from "./components/EpisodeRenameTaskSearchForm.vue";
import { useRenderIcon } from "@/components/ReIcon/src/hooks";
import AddFill from "@iconify-icons/ri/add-circle-line";

import { ApiResult } from "@/api/base";
import {
  loadPagedTasks,
  LoadPagedTasksReq,
  LoadPagedEpisodeRenameTaskItemsResp,
  PagedEpisodeRenameTaskItemDto
} from "@/api/renameTask";
import { EpisodeRenameTaskCardPropType } from "./props";

defineOptions({
  name: "EpisodeRenameTask"
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
const taskList = ref(new Array<EpisodeRenameTaskCardPropType>());
const searchConditions = ref({
  taskName: null,
  taskStatus: null
});

const getCardListData = async () => {
  const req: LoadPagedTasksReq = {
    current: pagination.value.current,
    pageSize: pagination.value.pageSize,
    condition: {
      taskName: searchConditions.value.taskName,
      taskStatus: searchConditions.value.taskStatus
    }
  };
  dataLoading.value = true;
  const resp: LoadPagedEpisodeRenameTaskItemsResp = await loadPagedTasks(req);
  dataLoading.value = false;
  if (!resp.success) {
    return;
  }

  const result: Array<EpisodeRenameTaskCardPropType> = resp.data.list.map(
    (item, index, array) => {
      return toRenameTaskCardPropType(item);
    }
  );
  taskList.value = result;
  pagination.value.current = resp.data.current;
  pagination.value.pageSize = resp.data.pageSize;
  pagination.value.total = resp.data.total;
};

function toRenameTaskCardPropType(
  dto: PagedEpisodeRenameTaskItemDto
): EpisodeRenameTaskCardPropType {
  return {
    id: dto.id,
    taskName: dto.taskName,
    taskStatus: dto.taskStatus,
    totalCount: dto.totalCount,
    pendingCount: dto.pendingCount,
    successCount: dto.successCount,
    failedCount: dto.failedCount
  };
}

onMounted(() => {
  getCardListData();
  // taskList.value = [
  //   {
  //     id: 1,
  //     taskName: "test",
  //     taskStatus: 0,
  //     totalCount: 1,
  //     pendingCount: 1,
  //     successCount: 1,
  //     failedCount: 1
  //   }
  // ];
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

const createFormVisible = ref(false);
function openCreateForm() {
  createFormVisible.value = true;
}
function closeCreateForm() {
  createFormVisible.value = false;
}

// 当创建任务成功时
function onCreateTaskSuccess(id: number) {
  toDetailPage({ id: id });
}

function onSearchClick(taskName: string, taskStatus: number) {
  searchConditions.value.taskName = taskName;
  searchConditions.value.taskStatus = taskStatus;

  // 当点击搜索按钮时，页码重置
  pagination.value.current = 1;
  getCardListData();
}
</script>

<template>
  <div>
    <div class="w-full">
      <el-row :gutter="20">
        <el-col :span="10"
          ><el-button :icon="useRenderIcon(AddFill)" @click="openCreateForm">
            创建任务
          </el-button>

          <!-- <rss-link-create-dialog-form
            v-if="createFormVisible"
            @cancel="closeCreateForm"
            @closeForm="closeCreateForm"
            @preview-success="onCreateTaskSuccess"
          /> -->
        </el-col>
        <el-col :span="14">
          <episode-rename-task-search-form
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
      <el-empty v-if="pagination.total <= 0" :description="`不存在任务`" />
      <template v-else>
        <el-space wrap :size="30" style="width: 100%">
          <template v-for="(task, index) in taskList" :key="index">
            <episode-rename-task-card
              :task-card="task"
              @manage-rss-bangumi="handleManageRssBangumi"
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
