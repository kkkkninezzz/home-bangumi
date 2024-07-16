<script setup lang="ts">
import { computed, PropType } from "vue";
import More2Fill from "@iconify-icons/ri/more-2-fill";

import { RssBangumiStatusEnum } from "../enums";

defineOptions({
  name: "ReCard"
});

interface RssBangumiCard {
  id: number;
  rssName: string; // rss链接名
  rssCategory: number; // 链接类型
  status: number; // 链接状态
  filterRules: Array<string>; // 过滤规则
  bangumiTitle: string; // 番剧标题
  posterUrl: string; // 番剧海报地址
  broadcastDayOfWeek: number; // 放送的星期几
  broadcast_date: number; //开始放送的日期
  season: string; // 放送的是哪一季
}

const props = defineProps({
  rssBangumiCard: {
    type: Object as PropType<RssBangumiCard>
  }
});

const emit = defineEmits(["manage-rss-link", "delete-item"]);

const handleClickManage = (rssBangumiCard: RssBangumiCard | undefined) => {
  emit("manage-rss-link", rssBangumiCard);
};

const handleClickDelete = (rssBangumiCard: RssBangumiCard | undefined) => {
  emit("delete-item", rssBangumiCard);
};

const cardClass = computed(() => [
  "list-card-item",
  {
    "list-card-item__disabled":
      props.rssBangumiCard?.status == RssBangumiStatusEnum.INACTIVE
  }
]);

const cardPosterClass = computed(() => [
  "list-card-item_detail--poster",
  {
    "list-card-item_detail--poster__disabled":
      props.rssBangumiCard?.status == RssBangumiStatusEnum.INACTIVE
  }
]);

// 判断是否是inactive状态
const rssBangumiActive = computed(
  () => props.rssBangumiCard?.status == RssBangumiStatusEnum.ACTIVE
);

const getImageUrl = name => {
  return new URL(`../../../assets/testimg/${name}`, import.meta.url).href;
};
</script>

<template>
  <div :class="cardClass">
    <div class="list-card-item_detail bg-bg_color">
      <el-container>
        <el-header height="35px">
          <div class="list-card-item_detail--operation">
            <el-dropdown trigger="click" :disabled="!rssBangumiActive">
              <IconifyIconOffline :icon="More2Fill" class="text-[24px]" />
              <template #dropdown>
                <el-dropdown-menu :disabled="!rssBangumiActive">
                  <el-dropdown-item @click="handleClickManage(rssBangumiCard)">
                    管理
                  </el-dropdown-item>
                  <el-dropdown-item @click="handleClickDelete(rssBangumiCard)">
                    删除
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
            <el-tag
              :color="rssBangumiActive ? '#00a870' : '#eee'"
              effect="dark"
              class="mx-1 list-card-item_detail--operation--tag"
            >
              {{ rssBangumiActive ? "已启用" : "已停用" }}
            </el-tag>
          </div>
        </el-header>
        <el-container>
          <el-aside width="200px">
            <div :class="cardPosterClass">
              <img :src="getImageUrl('test1.webp')" />
            </div>
          </el-aside>
          <el-main width="300px">
            <div class="list-card-item_detail--info">
              <el-row justify="space-between">
                <div
                  class="grid-content list-card-item_detail--rss_name text-text_color_primary"
                >
                  {{ rssBangumiCard?.rssName }}
                </div>
              </el-row>
              <el-row justify="space-between">
                <div
                  class="grid-content list-card-item_detail--bangumi_name text-text_color_primary"
                >
                  {{ rssBangumiCard?.bangumiTitle }}
                </div>
              </el-row>
              <el-row justify="space-between">
                <div
                  class="grid-content list-card-item_detail--rss_name text-text_color_primary"
                >
                  放送时间: 星期一
                </div>
              </el-row>
              <el-row justify="space-between">
                <div
                  class="grid-content list-card-item_detail--rss_name text-text_color_primary"
                >
                  首播时间: 2024-03-04
                </div>
              </el-row>
            </div>
          </el-main>
        </el-container>
      </el-container>
    </div>
  </div>
</template>

<style scoped lang="scss">
.list-card-item {
  display: flex;
  flex-direction: column;
  margin-bottom: 12px;
  overflow: hidden;
  cursor: pointer;
  border-radius: 3px;
  min-width: 650px;

  &_detail {
    flex: 1;
    min-height: 140px;
    padding: 24px 32px;

    &--poster {
      display: flex;
      align-items: center;
      justify-content: center;
      width: 200px;
      height: 280px;
      font-size: 26px;
      border-radius: 2%;

      &__disabled {
        color: #a1c4ff;
      }
    }

    &--info {
      height: 280px;
      font-size: 26px;
      color: #0052d9;
      border-radius: 2%;

      &__disabled {
        color: #a1c4ff;
      }
    }

    &--operation {
      display: flex;
      flex-direction: row-reverse;
      height: 100%;

      &--tag {
        border: 0;
      }
    }

    &--rss_name {
      margin: 0px 0 8px;
      font-size: 16px;
      font-weight: 400;
    }

    &--bangumi_name {
      margin: 0px 0 8px;
      font-size: 24px;
      font-weight: 400;
    }

    &--desc {
      display: -webkit-box;
      height: 40px;
      margin-bottom: 24px;
      overflow: hidden;
      font-size: 12px;
      line-height: 20px;
      text-overflow: ellipsis;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
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
../enums/index.js
