
import { makeRequest } from "core/utils/request";
import { useState } from "react";
import BaseForm from "../../BaseForm";
import "./styles.scss"

type FormState = {
    name: string;
    price:string;
    category: string;
}

const Form = () => {
    const [formData, setFormData] = useState<FormState>({
       name:'',
       price:'',
       category:''
    });

    const handleOnChange = (event: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const name = event.target.name;
        const value = event.target.value;
        setFormData(data=>({...data,[name]:value}));
    }
    const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        const payload={
            ...formData,
            imgURL:'Link',
            categories:[{id: formData.category}]
        }


        makeRequest({url:'/products', method: 'POST', data: payload});
    }

    return (
        <form onSubmit={handleSubmit}>
            <BaseForm title="CADASTRAR UM PRODUTO">
                <div className="row">
                    <div className="col-6">
                        <input value={formData.name} name="name" type="text"
                            className="form-control mb-5"
                            onChange={handleOnChange}
                            placeholder="Nome do produto"
                        />
                        <select value ={formData.category} onChange={handleOnChange}
                        className="form-control mb-5" name="category">
                            <option value="1">Livro</option>
                            <option value="3">Computadores</option>
                            <option value="2">Eletrônicos</option>
                        </select>
                        <input value={formData.price} name="price" type="text"
                            className="form-control"
                            onChange={handleOnChange}
                            placeholder="Preço"
                        />
                    </div>
                </div>
            </BaseForm>
        </form>
    )

}

export default Form;