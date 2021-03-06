import { useHistory } from "react-router";

const List =()=>{
    //useHistory é um hook para criar um pilha de navegação
    const history = useHistory();
    const handleCreate= ()=>{
        history.push("/admin/products/create");
    }

    return (
        <div className="admin-product-list">
            <button className="btn btn-primary btn-lg" onClick={handleCreate}>
                ADICIONAR
            </button>
        </div>
    );
}

export default List;