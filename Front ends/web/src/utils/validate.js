/**
 * Created by PanJiaChen on 16/11/18.
 */

/**
 * @param {string} path
 * @returns {Boolean}
 */
export function isExternal(path) {
  return /^(https?:|mailto:|tel:)/.test(path)
}

/**
 * @param {string} str
 * @returns {Boolean}
 * 校验用户名 只能字母和数字 1-30位
 */
export function validUsername(str) {
  var reg = /^[A-Za-z0-9]{1,30}$/;
  return reg.test(str);
}
