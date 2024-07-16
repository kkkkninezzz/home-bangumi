<script setup lang="ts">
import { computed, PropType } from "vue";
import More2Fill from "@iconify-icons/ri/more-2-fill";
import { ReText } from "@/components/ReText";
import {
  RssBangumiStatusEnum,
  RssBangumiStatusTagMap,
  BroadcastDayOfWeekTagMap,
  RssCategoryTagMap,
  RssBangumiHandleMethodEnum,
  CollectTag,
  ArchivedCollectTag
} from "../enums";
import { RssBangumiCardPropType } from "../props";

defineOptions({
  name: "RssBangumiCard"
});

const props = defineProps({
  rssBangumiCard: {
    type: Object as PropType<RssBangumiCardPropType>
  }
});

// 关闭表单时通知父组件
const emit = defineEmits<{
  (e: "manage-rss-bangumi", id: number): void;
  (e: "inactive-rss-bangumi", id: number): void;
  (e: "active-rss-bangumi", id: number): void;
  (e: "delete-rss-bangumi", id: number): void;
  (e: "archive-rss-bangumi", id: number): void;
}>();

const handleClickManage = (id: number) => {
  emit("manage-rss-bangumi", id);
};

const handleClickDelete = (id: number) => {
  emit("delete-rss-bangumi", id);
};

const handleClickInactive = (id: number) => {
  emit("inactive-rss-bangumi", id);
};

const handleClickActive = (id: number) => {
  emit("active-rss-bangumi", id);
};

const handleClickArchive = (id: number) => {
  emit("archive-rss-bangumi", id);
};

// 判断是否是inactive状态
const rssBangumiActive = computed(
  () => props.rssBangumiCard?.status == RssBangumiStatusEnum.ACTIVE
);

const getImageUrl = (posterUrl: string) => {
  if (posterUrl && posterUrl != "") {
    return import.meta.env.VITE_SERVER_BASE_URL + "/" + posterUrl;
  }
  return new URL("../../../assets/imgs/default-poster.png", import.meta.url)
    .href;
};

// 状态的 tag
const rssBangumiStatusTag = computed(() => {
  if (
    props.rssBangumiCard?.handleMethod == RssBangumiHandleMethodEnum.SUBSCRIBE
  ) {
    return RssBangumiStatusTagMap.get(props.rssBangumiCard?.status);
  }

  // 如果是归档的，那么展示归档状态的收集tag
  if (props.rssBangumiCard?.status == RssBangumiStatusEnum.ARCHIVED) {
    return ArchivedCollectTag;
  }

  return CollectTag;
});

// 显示放送的星期 tag
const broadcastDayOfWeekTag = computed(() =>
  BroadcastDayOfWeekTagMap.get(props.rssBangumiCard?.broadcastDayOfWeek)
);

// rss类别的 tag
const rssCategoryTag = computed(() =>
  RssCategoryTagMap.get(props.rssBangumiCard.rssCategory)
);

// 展示inactive按钮
const showInactiveButton = computed(
  () =>
    props.rssBangumiCard?.handleMethod ==
      RssBangumiHandleMethodEnum.SUBSCRIBE &&
    props.rssBangumiCard?.status == RssBangumiStatusEnum.ACTIVE
);

// 展示active按钮
const showActiveButton = computed(
  () =>
    props.rssBangumiCard?.handleMethod ==
      RssBangumiHandleMethodEnum.SUBSCRIBE &&
    props.rssBangumiCard?.status == RssBangumiStatusEnum.INACTIVE
);
// 展示归档按钮
const showArchiveButton = computed(
  () => props.rssBangumiCard?.status != RssBangumiStatusEnum.ARCHIVED
);

const dateFormat = (dateMills: number) => {
  const date = new Date(dateMills);
  // 从 Date 对象中获取年、月和日
  const year: number = date.getFullYear(); // 获取年份
  const month: number = date.getMonth() + 1; // 获取月份（注意：月份从 0 开始）
  const day: number = date.getDate(); // 获取日

  // 格式化日期为 "YY/MM/DD" 格式
  const formattedDate: string = `${year.toString().slice(2)}/${month.toString().padStart(2, "0")}/${day.toString().padStart(2, "0")}`;
  return formattedDate;
};
</script>

<template>
  <el-card style="width: 300px; height: 500px" class="list-card-item">
    <template #header>
      <div class="list-card-item_detail--header_row">
        <el-row>
          <el-col :span="16" class="grid-content">
            <ReText class="list-card-item_detail--title text-[24px]">
              {{ rssBangumiCard.bangumiTitle }}
            </ReText>
          </el-col>
          <el-col v-if="rssBangumiCard.season" :span="4" class="grid-content">
            <el-tag
              color="#faaa3b"
              effect="dark"
              class="list-card-item_detail--tag"
              >第{{
                rssBangumiCard.season ? rssBangumiCard.season : 0
              }}季</el-tag
            >
          </el-col>
          <el-col :span="4">
            <div class="list-card-item_detail--operation">
              <el-dropdown trigger="click">
                <IconifyIconOffline :icon="More2Fill" class="text-[24px]" />
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item
                      v-if="showInactiveButton"
                      @click="handleClickInactive(rssBangumiCard.id)"
                    >
                      禁用</el-dropdown-item
                    >
                    <el-dropdown-item
                      v-if="showActiveButton"
                      @click="handleClickActive(rssBangumiCard.id)"
                    >
                      启用
                    </el-dropdown-item>

                    <el-dropdown-item
                      v-if="showArchiveButton"
                      @click="handleClickArchive(rssBangumiCard.id)"
                    >
                      归档
                    </el-dropdown-item>
                    <el-dropdown-item
                      @click="handleClickDelete(rssBangumiCard.id)"
                    >
                      删除
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
          </el-col>
        </el-row>
      </div>
    </template>
    <div class="list-card-item_detail--poster">
      <img
        :src="getImageUrl(rssBangumiCard.posterUrl)"
        style="width: 100%"
        @click="handleClickManage(rssBangumiCard.id)"
      />
    </div>
    <template #footer>
      <div class="flex gap-2">
        <el-tag
          v-if="rssBangumiStatusTag"
          :color="rssBangumiStatusTag.color"
          effect="dark"
          class="list-card-item_detail--tag"
        >
          {{ rssBangumiStatusTag.content }}
        </el-tag>
        <el-tag
          v-if="rssCategoryTag"
          :color="rssCategoryTag.color"
          effect="dark"
          class="list-card-item_detail--tag"
          >{{ rssCategoryTag.content }}</el-tag
        >
        <el-tag
          v-if="rssBangumiCard.broadcastDate"
          effect="dark"
          class="list-card-item_detail--tag"
          >{{ dateFormat(rssBangumiCard.broadcastDate) }}</el-tag
        >
        <el-tag
          v-if="broadcastDayOfWeekTag"
          :color="broadcastDayOfWeekTag.color"
          effect="dark"
          class="list-card-item_detail--tag"
          >{{ broadcastDayOfWeekTag.content }}</el-tag
        >
      </div>
    </template>
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
      width: 250px;
      height: 340px;

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
