<script setup lang="ts">
import { computed } from "vue";
import { ReText } from "@/components/ReText";
import {
  CloseBold,
  WarningFilled,
  InfoFilled,
  WarnTriangleFilled
} from "@element-plus/icons-vue";
import { AlterMessageCardPropType } from "../props";
import * as rssbangumiHooks from "@/views/rssbangumi/components/hooks";
import * as renametaskHooks from "@/views/renametask/components/hooks";
import { formatDateTime } from "@/utils/date";
import { MessageCategoryEnum, MessageTypeEnum } from "@/views/message/enums";
import { readMessage } from "@/api/message";
import { ApiResult } from "@/api/base";

defineOptions({
  name: "AlterMessageCard"
});
const props = defineProps({
  messageCard: {
    type: Object as PropType<AlterMessageCardPropType>
  }
});

// 按钮事件通知父组件
const emit = defineEmits<{
  (e: "read", messageId: number): void;
}>();

const { toDetailPage: rssBangumiToDetailPage } =
  rssbangumiHooks.useDetailPage();
const { toDetailPage: renameTaskToDetailPage } =
  renametaskHooks.useDetailPage();

const handleClick = () => {
  if (props.messageCard.category == MessageCategoryEnum.RSS_BANGUMI_EPISODE) {
    if (props.messageCard.subjectId) {
      // 跳转到番剧详情页
      rssBangumiToDetailPage({ id: props.messageCard.subjectId });
    }
  } else if (
    props.messageCard.category == MessageCategoryEnum.EPISODE_RENAME_TASK
  ) {
    if (props.messageCard.subjectId) {
      // 跳转到重命名任务详情页
      renameTaskToDetailPage({ id: props.messageCard.subjectId });
    }
  }
};
const handleReadClick = async () => {
  const resp: ApiResult = await readMessage(props.messageCard.id);
  if (!resp.success) {
    return;
  }
  emit("read", props.messageCard.id);
};
</script>

<template>
  <el-card class="message-item" shadow="hover">
    <div class="message-header">
      <el-link @click="handleClick">
        <re-text>
          <el-icon>
            <WarningFilled v-if="messageCard.type == MessageTypeEnum.WARNING" />
            <InfoFilled v-else-if="messageCard.type == MessageTypeEnum.INFO" />

            <WarnTriangleFilled
              v-if="messageCard.type == MessageTypeEnum.ERROR"
            />
          </el-icon>
          {{ messageCard.title }}
        </re-text>
      </el-link>
      <el-button
        size="small"
        :icon="CloseBold"
        text
        class="mark-read-btn"
        @click="handleReadClick"
      />
    </div>

    <div class="message-content">
      <re-text :lineClamp="3">
        {{ messageCard.content }}
      </re-text>
    </div>

    <div class="message-footer">
      <re-text class="message-time">{{
        formatDateTime(messageCard.createdTime)
      }}</re-text>
    </div>
  </el-card>
</template>

<style scoped>
.message-item {
  border: 1px solid #ebebeb;
  border-radius: 8px;
  padding: 3px;
  width: 100%;
  min-width: 200px;
}

.message-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.mark-read-btn {
  margin-left: auto;
}

.message-content {
  margin-bottom: 12px;
}

.message-time {
  color: #909399;
}
</style>
