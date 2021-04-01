import axios,{Method} from 'axios';

type RequestParams = {
    //metodo com os verbos do axios (ex: get/post etc) sina ? tornar n obrigatorio o parametro
    method?:Method;
    url:string;
    data?:object;
    params?:object;
};
//endereço de callback usando o proxy do package.json para converter
const BASE_URL = 'http://localhost:3000';
export const makeRequest =({method='GET',url,data,params}:RequestParams)=>{
    return axios ({
        method,
        //anotação usando teamplate string
        url: `${BASE_URL}${url}`,
        //corpo da requisição
        data,
        params
    });
};