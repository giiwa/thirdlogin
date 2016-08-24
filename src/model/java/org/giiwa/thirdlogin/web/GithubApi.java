package org.giiwa.thirdlogin.web;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.giiwa.core.bean.X;
import org.giiwa.core.conf.Global;

public class GithubApi {

  private static final String AUTHORIZE_URL    = "https://github.com/login/oauth/authorize?client_id=%s&redirect_uri=%s&state=%s";
  private static final String ACCESS_TOKEN_URL = "https://github.com/login/oauth/access_token?state=%s";

  private final String        githubState;

  public GithubApi(String state) {
    this.githubState = state;
  }

  public String getAuthorizationUrl() {
    try {
      return String.format(AUTHORIZE_URL, Global.getString("thirdlogin.github.clientid", X.EMPTY),
          URLEncoder.encode(Global.getString("thirdlogin.callback", X.EMPTY), "UTF8"), githubState);
    } catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }

  // @Override
  // public String getAccessTokenEndpoint() {
  // return String.format(ACCESS_TOKEN_URL, githubState);
  // }

}
