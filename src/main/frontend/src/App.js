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



export default function Index( props ) {
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
                               <Link href="/">
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
                               <Link href="#">
                               <ListItemButton>
                                    <ListItemIcon>
                                   <WarehouseIcon />
                                 </ListItemIcon>
                                 <ListItemText primary="제품" />
                                </ListItemButton>
                                </Link>
                                </ListItem>
                                <ListItem disablePadding>
                               <Link href="#">
                               <ListItemButton>
                                    <ListItemIcon>
                                   <WarehouseIcon />
                                 </ListItemIcon>
                                 <ListItemText primary="판매/승인" />
                                </ListItemButton>
                                </Link>
                             </ListItem>
                           </List>
                         </nav>
                       </Box>
                </div>
                <div className="main-content">
                    <Routes >
                        <Route path="/" element = { <Main /> } />
                        <Route path="/component/material/Material" element = { <Material/> } />
                        <Route path="/component/material/MaterialInoutList/:matID" element = { <MaterialInoutList/> } />
                    </Routes>

                </div>
            </div>
        </BrowserRouter>
    </> );
}
