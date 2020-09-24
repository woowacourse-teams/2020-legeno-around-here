export const BASE_URL = process.env.REACT_APP_SERVER_HOST;
export const OAUTH2_REDIRECT_URI = BASE_URL + '/oauth2/redirect';

export const API_BASE_URL = process.env.REACT_APP_API_SERVER_HOST;
export const GOOGLE_AUTH_URL = API_BASE_URL + '/oauth2/authorize/google?redirect_uri=' + OAUTH2_REDIRECT_URI;
export const GITHUB_AUTH_URL = API_BASE_URL + '/oauth2/authorize/github?redirect_uri=' + OAUTH2_REDIRECT_URI;
