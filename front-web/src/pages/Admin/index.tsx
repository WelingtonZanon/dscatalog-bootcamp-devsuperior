import { Route, Switch } from "react-router-dom";
import Navbar from "./components/Navbar";
import Products from "./components/products";
import "./styles.scss"

const Admin = ()=>(
    <div className="admin-container">
        <Navbar/>
        <div className="admin-content">
            <Switch>
                <Route path="/admin/products">
                    <Products/>
                </Route>
                <Route path="/admin/categories">
                    <h1>categories</h1>
                </Route>
                <Route path="/admin/users">
                    <h1>Users</h1>
                </Route>
            </Switch>
        </div>
    </div>
);

export default Admin;