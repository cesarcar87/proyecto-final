import { LOGIN_REQUEST, LOGOUT_REQUEST, SUCCESS, FAILURE } from "./AuthTypes";

const initialState = {
  username: "",
  isLoggedIn: "",
};

const authReducer = (state = initialState, action) => {
  switch (action.type) {
    case LOGIN_REQUEST:
    case LOGOUT_REQUEST:
      return {
        ...state,
      };
    case SUCCESS:
    case FAILURE:
      return {
        username: action.payload.username,
        isLoggedIn: action.payload.isLoggedIn,
      };
    default:
      return state;
  }
};

export default authReducer;