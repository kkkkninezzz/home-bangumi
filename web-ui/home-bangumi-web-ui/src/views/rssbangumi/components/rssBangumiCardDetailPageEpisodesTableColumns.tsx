import {
  RssBangumiEpisodeStatusTagMap,
  DownloaderCategoryTagMap
} from "../enums";
import { dateFormat } from "@pureadmin/utils";
import { formatDateTime } from "@/utils/date";

export const EpisodeColumns: TableColumnList = [
  {
    label: "id",
    prop: "id",
    hide: true
  },
  {
    label: "剧集",
    prop: "episodeNo",
    width: "100",
    fixed: true
  },
  {
    label: "状态",
    prop: "status",
    width: "140",
    fixed: true,
    cellRenderer: ({ row, index }) => (
      <>
        <el-tag type={RssBangumiEpisodeStatusTagMap.get(row.status).tagType}>
          {RssBangumiEpisodeStatusTagMap.get(row.status).tagContent}
        </el-tag>
      </>
    )
  },
  {
    label: "原始标题",
    prop: "rawEpisodeTitle",
    width: "400"
  },
  {
    label: "剧集文件名",
    prop: "episodeFileName",
    width: "400"
  },
  {
    label: "重命名后的剧集文件名",
    prop: "renamedEpisodeFileName",
    width: "320"
  },
  {
    label: "种子发布时间",
    prop: "torrentPubDate",
    width: "240",
    formatter: (row, column, cellValue, index) => {
      if (row.torrentPubDate) {
        return formatDateTime(row.torrentPubDate);
      }
      return "";
    }
  },
  {
    label: "下载器",
    prop: "downloaderCategory",
    width: "100",
    cellRenderer: ({ row, index }) => (
      <>
        <el-tag
          type={DownloaderCategoryTagMap.get(row.downloaderCategory).tagType}
        >
          {DownloaderCategoryTagMap.get(row.downloaderCategory).tagContent}
        </el-tag>
      </>
    )
  },
  {
    label: "下载标识",
    prop: "torrentIdentity",
    width: "200"
  },
  {
    label: "错误消息",
    prop: "errorMessage",
    width: "200"
  },
  {
    label: "操作",
    width: "180",
    fixed: "right",
    slot: "operation"
  }
];
