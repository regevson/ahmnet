export const funcs = {
  calcWeekNum(date) {
    const onejan = new Date(date.getFullYear(), 0, 1);
    return Math.ceil((((date.getTime() - onejan.getTime()) / 86400000) + onejan.getDay()-1) / 7) - 1;
  },

  splitCamelCase(camel) {
    var reg = /([a-z0-9])([A-Z])/g;
    let tmp = camel.replace(reg, '$1 $2');
    return tmp.replace(/[0-9]/g, '');
  },

}
