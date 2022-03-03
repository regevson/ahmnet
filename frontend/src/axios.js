import axios from 'axios'
import router from './router.js'

axios.defaults.baseURL = 'http://10.0.0.242:8080/';
axios.defaults.headers.common['Authorization'] = 'Bearer ' + sessionStorage.getItem('accessToken');


export async function axiosReq(req, params, config) {
    let res;

    try {
      if(params == null)
        res = await axios.get('api/' + req);
      else
        res = await axios.post('api/' + req, params, config);

    } catch(err) { 
        alert(err.response.data); 
        router.replace({name: 'timetable'}); 
        router.go(); 
        return null; 
    }

    return res;
}


export const ax = {

  error(err) {
    alert(err.response.data); 
    router.replace({name: 'timetable'}); 
    router.go(); 
  },

  async get(req) {
    try {
      return await axios.get('api/' + req);
    } catch(err) {
      this.error(err);
    }
  },

  async delete(req) {
    try {
      return await axios.delete('api/' + req);
    } catch(err) {
      this.error(err);
    }
  },

  async post(req, params) {
    try {
      return await axios.post('api/' + req, params);
    } catch(err) {
      this.error(err);
    }
  },

  async put(req, params) {
    try {
      return await axios.put('api/' + req, params);
    } catch(err) {
      this.error(err);
    }
  },

}
