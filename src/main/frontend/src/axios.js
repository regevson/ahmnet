import axios from 'axios'

axios.defaults.baseURL = 'http://10.0.0.242:8080/';
axios.defaults.headers.common['Authorization'] = 'Bearer ' + localStorage.getItem('accessToken');

