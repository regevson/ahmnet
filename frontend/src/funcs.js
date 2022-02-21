export const funcs = {
  calcWeekNum(date) {
    const onejan = new Date(date.getFullYear(), 0, 1);
    return Math.ceil((((date.getTime() - onejan.getTime()) / 86400000) + onejan.getDay()-1) / 7) - 1;
  },
}
