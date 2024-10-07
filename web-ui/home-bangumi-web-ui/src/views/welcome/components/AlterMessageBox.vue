<script setup lang="ts">
import { shallowRef, onBeforeMount } from "vue";
import AlterMessageCard from "./AlterMessageCard.vue";
import { getMessages, MessagesResp, MessageDto } from "@/api/message";
import { AlterMessageCardPropType } from "../props";
import Bell from "@iconify-icons/ep/bell";

defineOptions({
  name: "AlterMessageBox"
});

const messageList = shallowRef(new Array<AlterMessageCardPropType>());

const initMessageList = async () => {
  const resp: MessagesResp = await getMessages();
  if (!resp.success) {
    return;
  }
  messageList.value = resp.data.messages.map((e, i) => {
    return {
      id: e.id,
      category: e.category,
      type: e.type,
      read: e.read,
      title: e.title,
      content: e.content,
      subjectId: e.subjectId,
      createdTime: e.createdTime
    };
  });
};

onBeforeMount(() => {
  initMessageList();
});

function handleReadMessage(messageId: number) {
  messageList.value = messageList.value.filter(item => {
    return item.id != messageId;
  });
}
</script>

<template>
  <el-card shadow="never">
    <div class="flex justify-between alter-message-box-header">
      <span class="text-md font-medium">通知</span>
    </div>
    <div class="alter-message-box-content">
      <el-empty v-if="messageList.length == 0" description="no data" />
      <el-scrollbar v-else max-height="800px">
        <el-space
          direction="vertical"
          :size="20"
          fill
          style="width: 100%; min-width: 200px"
        >
          <template v-for="(message, index) in messageList">
            <alter-message-card
              :message-card="message"
              @read="handleReadMessage"
            />
          </template>
        </el-space>
      </el-scrollbar>
    </div>
    <!--         
    <el-container>
      <el-header height="200">
        <span> 通知 </span>
      </el-header>
      <el-main>
        <div style="height: 800px">
          <el-empty v-if="messageList.length == 0" description="no data" />
          <el-scrollbar v-else max-height="800px">
            <el-space direction="vertical" :size="20">
              <template v-for="(message, index) in messageList">
                <alter-message-card
                  :message-card="message"
                  @read="handleReadMessage"
                />
              </template>
            </el-space>
          </el-scrollbar>
        </div>
      </el-main>
    </el-container> -->
  </el-card>
</template>

<style lang="scss" scoped>
.alter-message-box-header {
  margin-bottom: 15px;
}

.alter-message-box-content {
  height: 800px;
}
</style>
