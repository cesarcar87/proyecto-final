import { combineReducers } from "redux";
import userReducer from "./User/UserReducer";
import authReducer from "./User/Auth/AuthReducer";

const rootReducer = combineReducers({
  user: userReducer,
  auth: authReducer,
});

export default rootReducer;