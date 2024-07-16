

/**
 * 将时间戳格式化为YYYY-MM-DD HH:mm:ss
 */
export function formatDateTime(dateMills: number) : string {
  const date = new Date(dateMills);
  const year = date.getFullYear();  
  const month = (date.getMonth() + 1).toString().padStart(2, '0');  
  const day = date.getDate().toString().padStart(2, '0'); 
  const hour = date.getHours().toString().padStart(2, '0');
  const minute = date.getMinutes().toString().padStart(2, '0');
  const second = date.getSeconds().toString().padStart(2, '0');
  const formattedDate = `${year}-${month}-${day} ${hour}:${minute}:${second}`;  
  return formattedDate;
}

/**
 * 将时间戳格式化为YYYY-MM-DD
 */
export function formatDate(dateMills: number) : string {
  const date = new Date(dateMills);
  const year = date.getFullYear();  
  const month = (date.getMonth() + 1).toString().padStart(2, '0');  
  const day = date.getDate().toString().padStart(2, '0'); 
  const formattedDate = `${year}-${month}-${day}`;  
  return formattedDate;
}

/**
 * 是否早于本周一
 */
export function isEarlierThanThisMonday(timestamp: number): boolean {
    const now = new Date();
    const today = new Date(now.getFullYear(), now.getMonth(), now.getDate());
    const dayOfWeek = today.getDay();
    const daysToMonday = (dayOfWeek === 0 ? 7 : dayOfWeek) - 1;
    const thisMonday = new Date(today);
    thisMonday.setDate(today.getDate() - daysToMonday);

    const inputDate = new Date(timestamp);

    return inputDate < thisMonday;
}