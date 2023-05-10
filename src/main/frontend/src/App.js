import React from 'react';
import { BrowserRouter , Routes , Route } from "react-router-dom";
import Header from "./component/Header";
import "./App.css";
import Box from '@mui/material/Box';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import Divider from '@mui/material/Divider';
import InboxIcon from '@mui/icons-material/Inbox';
import DraftsIcon from '@mui/icons-material/Drafts';
import WarehouseIcon from '@mui/icons-material/Warehouse';
import Material from "./component/material/Material";
import Link from '@mui/material/Link';
import Main from "./Main";
import MaterialInoutList from "./component/material/MaterialInoutList";

/*--------------------------- 제품 부분 ------------------------------*/
import ProductTab from "./component/product/ProductTab";
import CreateProduct from "./component/product/CreateProduct";
import PlanProduct from "./component/product/PlanProduct";
import ProcessProduct from "./component/product/ProcessProduct";
import ManageProduct from "./component/product/ManageProduct";
/*--------------------------- 멤버 부분 ------------------------------*/
import Login from "./component/member/Login";
import AllowApproval from "./component/member/AllowApproval";
import AllowMaterial from "./component/member/AllowMaterial";
import AllowProduct from "./component/member/AllowProduct";
import AllowSales from "./component/member/AllowSales";


/* --------------------------- 판매 부분 --------------------------- */
import SalesHeader from "./component/sales/SalesHeader";




export default function Index( props ) {
    const member = sessionStorage.getItem('member')
    const urlHome = member !=null ? "/component/member/Info" : "/component/member/Login";

    return ( <>
        <BrowserRouter>
            <div className="header">
                <Header />
            </div>
            <div className="content">
                <div className="sidebar">
                   <Box >
                         <nav aria-label="main mailbox folders">
                           <List>
                             <ListItem disablePadding>
                               <Link href={urlHome}>
                               <ListItemButton>
                                    <ListItemIcon>
                                   <WarehouseIcon />
                                 </ListItemIcon>
                                 <ListItemText primary="HOME" />
                                </ListItemButton>
                                </Link>
                             </ListItem>
                             <ListItem disablePadding>
                             <Link href="/component/material/Material">
                              <ListItemButton>
                              <ListItemIcon>
                              <WarehouseIcon />
                              </ListItemIcon>
                              <ListItemText primary="자재" />
                              </ListItemButton>
                              </Link>
                              </ListItem>
                                <ListItem disablePadding>
                               <Link href="/component/product/productTab">
                               <ListItemButton>
                                    <ListItemIcon>
                                   <WarehouseIcon />
                                 </ListItemIcon>
                                 <ListItemText primary="제품" />
                                </ListItemButton>
                                </Link>
                                </ListItem>
                                <ListItem disablePadding>
                               <Link href="/component/sales/SalesHeader">
                               <ListItemButton>
                                    <ListItemIcon>
                                   <WarehouseIcon />
                                 </ListItemIcon>
                                 <ListItemText primary="판매" />
                                </ListItemButton>
                                </Link>
                             </ListItem>
                             <ListItem disablePadding>
                                <Link href="/component/member/AllowApproval">
                                <ListItemButton>
                                     <ListItemIcon>
                                    <WarehouseIcon />
                                  </ListItemIcon>
                                  <ListItemText primary="승인" />
                                 </ListItemButton>
                                 </Link>
                              </ListItem>
                           </List>
                         </nav>
                       </Box>
                </div>
                <div className="main-content">
                    <Routes >
                        <Route path="/component/member/Login" element = { <Login /> } />
                        <Route path="/component/material/Material" element = { <Material/> } />
                        <Route path="/component/product/ProductTab" element ={<ProductTab/>}/>
                        <Route path="/component/material/MaterialInoutList/:matID" element = { <MaterialInoutList/> } />
                    </Routes>

                </div>
            </div>
        </BrowserRouter>
    </> );
}
