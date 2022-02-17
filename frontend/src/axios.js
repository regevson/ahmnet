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
