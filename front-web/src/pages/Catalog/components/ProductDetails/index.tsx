import { useEffect, useState } from 'react';
import { useParams } from 'react-router';
import { Link } from 'react-router-dom';
import { ReactComponent as ArrowIcon } from 'core/assets/images/arrow.svg'
import ProductPrice from 'core/components/ProductPrice';
import { Product } from 'core/types/Product';
import { makeRequest } from 'core/utils/request';
import ProductDescriptionLoader from '../Loaders/ProductDescriptionLoader';
import ProductInfoLoader from '../Loaders/ProductInfoLoader';

import './style.scss'

type ParamsType = {
    productId: string;
}
const ProductDetails = () => {
    const { productId } = useParams<ParamsType>();
    const [product, setProduct] = useState<Product>();
    const [isLoading, setIsLoading] = useState(false);


    useEffect(() => {
        setIsLoading(true);
        makeRequest({ url: `/products/${productId}` })
            .then(response => setProduct(response.data))
            .finally(() => setIsLoading(false));
    }, [productId]);


    return (
        <div className="product-details-container">
            <div className="card-base border-radios-20 product-details">
                <Link to='/products' className="products-details-goback">
                    <ArrowIcon className="icon-goback" />
                    <h1 className="text-goback">voltar</h1>
                </Link>
                <div className="row">
                    <div className="col-6 pr-5">
                        {isLoading ? <ProductInfoLoader /> : (
                            <>
                                <div className="product-details-card border-radios-20 text-center">
                                    <img src={product?.imgUrl} alt={product?.imgUrl} className="product-details-image" />
                                </div>
                                <h1 className="product-details-name">
                                    {product?.name}
                                </h1>
                                {product?.price && <ProductPrice price={product?.price} />}
                            </>
                        )}
                    </div>
                    <div className="col-6 product-details-card border-radios-20">
                        {isLoading ? <ProductDescriptionLoader /> : (
                            <>
                                <h1 className="procut-description-title">
                                    Decri????o do Produto
                        </h1>
                                <p className="product-descripiton-text">
                                    {product?.description}
                                </p>
                            </>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );

};


export default ProductDetails;