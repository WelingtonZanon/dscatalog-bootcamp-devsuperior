import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import ProductCard from './components/ProductCard';
import './styles.scss';
import { makeRequest } from '../../core/utils/request';
import { ProductsReponse } from '../../core/types/Product';
const Catalog = () => {

    const [productsResponse, setProductsReponse] = useState<ProductsReponse>();
    //console.log(productsResponse);
    /* useEffect é Hook (função) para acessa ciclo de
     vida do pomponente. Recebe uma função e um array.
    **/
    useEffect(()=>{
    /*fetch (api) JS para entregar response entity (promises)
    Limitações - muito verboso/No progress UPload e No query strings nativo
    
        fetch('http://localhost:3000/products')
        .then(response => response.json())
        .then(response => console.log(response));
    forma inline de usar o axios    
        axios('http://localhost:3000/products')
            .then(response => console.log(response));
     makeRequest forma mais profissional de usar o axios
     criando um core para chamar em execução       
         **/ 
        const params = {
            page:0,
            linesPerPage:12
        }
            makeRequest({url:'/products',params})
            .then(response => setProductsReponse(response.data));
    },[]);

    return (

        <div className="catalog-container">
            <h1 className='catalog-title'>
                Catálogo de Produtos
                </h1>
            <div className="catalog-products">
                
                {productsResponse?.content.map(product =>(
                    //passar key obrigatoria para a listagem
                    <Link to={`products/${product.id}`} key={product.id}>
                    <ProductCard product={product} />
                    </Link>
                ))}
               
            </div>
        </div>
    );
};

export default Catalog;