<script setup lang="ts">
import { computed, PropType } from "vue";
import More2Fill from "@iconify-icons/ri/more-2-fill";
import { EpisodeRenameTaskStatusTagMap } from "../enums";
import { EpisodeRenameTaskCardPropType } from "../props";
import { ReText } from "@/components/ReText";

defineOptions({
  name: "EpisodeRenameTaskCard"
});

const props = defineProps({
  taskCard: {
    type: Object as PropType<EpisodeRenameTaskCardPropType>
  }
});

// 关闭表单时通知父组件
const emit = defineEmits<{
  (e: "manage-task", id: number): void;
  (e: "delete-task", id: number): void;
}>();

const handleClickManage = (id: number) => {
  emit("manage-task", id);
};

const handleClickDelete = (id: number) => {
  emit("delete-task", id);
};

// 状态的 tag
const taskStatusTag = computed(() => {
  return EpisodeRenameTaskStatusTagMap.get(props.taskCard?.taskStatus);
});
</script>

<template>
  <el-card style="width: 280px; height: 260px" class="list-card-item">
    <template #header>
      <div class="list-card-item_detail--header_row">
        <el-row>
          <el-col :span="18" class="grid-content">
            <ReText class="list-card-item_detail--title text-[24px]">
              {{ taskCard.taskName }}
            </ReText>
          </el-col>
          <el-col :span="6" class="grid-content">
            <el-tag
              :color="taskStatusTag.color"
              effect="dark"
              class="list-card-item_detail--tag"
              >{{ taskStatusTag.content }}</el-tag
            >
          </el-col>
        </el-row>
      </div>
    </template>
    <div class="list-card-item_detail--poster">
      <el-descriptions
        direction="vertical"
        :column="2"
        border
        @click="handleClickManage(taskCard.id)"
      >
        <el-descriptions-item label="总数" width="110">{{
          taskCard.totalCount
        }}</el-descriptions-item>
        <el-descriptions-item label="等待中" width="110">{{
          taskCard.pendingCount
        }}</el-descriptions-item>
        <el-descriptions-item label="成功" width="110">{{
          taskCard.successCount
        }}</el-descriptions-item>
        <el-descriptions-item label="失败" width="110">{{
          taskCard.failedCount
        }}</el-descriptions-item>
      </el-descriptions>
    </div>
  </el-card>
</template>

<style scoped lang="scss">
.list-card-item {
  display: flex;
  flex-direction: column;
  margin-bottom: 24px;
  border-radius: 3px;

  &_detail {
    flex: 1;
    padding: px 32px;

    &--header_row {
      height: 20px;
    }

    &--title {
      width: 100%;
      line-height: 30px;
      height: 30px;
      // margin: 0px 0 8px;
      font-weight: 400;
    }

    &--operation {
      cursor: pointer;
      display: flex;
      flex-direction: row-reverse;
      height: 100%;
    }

    &--tag {
      border: 0;
    }

    &--poster {
      cursor: pointer;
      display: flex;
      align-items: center;
      justify-content: center;

      border-radius: 2%;

      &__disabled {
        color: #a1c4ff;
      }
    }

    &--info {
      height: 100%;
      font-size: 14px;

      border-radius: 2%;

      &__disabled {
        color: #a1c4ff;
      }
    }
  }

  &__disabled {
    .list-card-item_detail--name,
    .list-card-item_detail--desc {
      color: var(--el-text-color-disabled);
    }

    .list-card-item_detail--operation--tag {
      color: #bababa;
    }
  }
}
</style>
