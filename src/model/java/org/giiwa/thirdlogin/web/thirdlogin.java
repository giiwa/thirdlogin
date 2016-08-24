package org.giiwa.thirdlogin.web;

import org.giiwa.core.base.Http;
import org.giiwa.core.base.Http.Response;
import org.giiwa.core.bean.X;
import org.giiwa.core.bean.Helper.V;
import org.giiwa.core.conf.Global;
import org.giiwa.core.json.JSON;
import org.giiwa.framework.bean.Role;
import org.giiwa.framework.bean.User;
import org.giiwa.framework.web.Model;
import org.giiwa.framework.web.Path;

public class thirdlogin extends Model {

  private static String github_token = "https://github.com/login/oauth/access_token";
  private static String github_user  = "https://api.github.com/user?access_token=%s";
  private static String wechat_token = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
  private static String wechat_user  = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s";
  private static String sina_token   = "https://api.weibo.com/oauth2/access_token";
  private static String sina_user    = "https://api.weibo.com/2/users/show.json?access_token=%s&uid=%s";

  @Path()
  public void onGet() {

  }

  @Path(path = "sina", log = Model.METHOD_GET | Model.METHOD_POST)
  public void sina() {
    String code = this.getString("code");
    JSON jo = JSON.create();
    jo.put("client_id", Global.getString("thirdlogin.sina.appkey", X.EMPTY));
    jo.put("client_secret", Global.getString("thirdlogin.sina.secret", X.EMPTY));
    jo.put("code", code);
    jo.put("redirect_uri", Global.getString("site.url", X.EMPTY) + "/thridlogin/sina");

    Response r = Http.post(sina_token, jo);
    jo = JSON.fromObject(r.body);
    if (jo.has("access_token")) {
      String token = jo.getString("access_token");
      String uid = jo.getString("uid");
      User u = User.load("sina//" + uid);
      if (u == null) {

        String url = String.format(sina_user, token, uid);
        r = Http.get(url);
        jo = JSON.fromObject(r.body);
        String nickname = jo.getString("screen_name");
        String photo = jo.getString("profile_image_url");
        V v = V.create("name", "sina//" + uid);
        v.set("nickname", nickname);
        v.set("photo", photo);
        long id = User.create(v);
        u = User.loadById(id);

        String role = Global.getString("user.role", "N/A");
        Role r1 = Role.loadByName(role);
        if (r != null) {
          u.setRole(r1.getId());
        }
      }

      this.setUser(u);
      this.redirect("/user/go");
      return;
    }

    this.println(r.body);
  }

  @Path(path = "wechat", log = Model.METHOD_GET | Model.METHOD_POST)
  public void wechat() {
    String code = this.getString("code");
    String url = String.format(wechat_token, Global.getString("thirdlogin.wechat.appid", X.EMPTY),
        Global.getString("thirdlogin.wechat.secret", X.EMPTY), code);

    Response r = Http.get(url);
    JSON jo = JSON.fromObject(r.body);
    if (jo.has("access_token")) {
      String token = jo.getString("access_token");
      String openid = jo.getString("openid");
      String unionid = jo.getString("unionid");

      User u = User.load("wechat//" + unionid);
      if (u == null) {

        url = String.format(wechat_user, token, openid);
        r = Http.get(url);

        jo = JSON.fromObject(r.body);
        String nickname = jo.getString("nickname");
        String photo = jo.getString("headimgurl");

        V v = V.create();
        v.set("name", "wechat//" + unionid);
        v.set("nickname", nickname);
        v.set("photo", photo);
        long id = User.create(v);
        u = User.loadById(id);

        String role = Global.getString("user.role", "N/A");
        Role r1 = Role.loadByName(role);
        if (r != null) {
          u.setRole(r1.getId());
        }

      }
      this.setUser(u);
      this.redirect("/user/go");
      return;
    }

    this.println(r.body);
  }

  @Path(path = "github", log = Model.METHOD_GET | Model.METHOD_POST)
  public void github() {
    String code = this.getString("code");
    JSON jo = JSON.create();
    jo.put("client_id", Global.getString("thirdlogin.github.clientid", X.EMPTY));
    jo.put("client_secret", Global.getString("thirdlogin.github.secret", X.EMPTY));
    jo.put("code", code);

    JSON h1 = JSON.create();
    h1.put("Accept", "application/json");
    Response r = Http.post(github_token, null, h1, jo);
    jo = JSON.fromObject(r.body);
    if (jo.has("access_token")) {
      String token = jo.getString("access_token");
      String url = String.format(github_user, token);
      r = Http.get(url);
      jo = JSON.fromObject(r.body);

      if (jo.has("login")) {
        String name = jo.getString("login");
        String photo = jo.getString("avatar_url");
        String nickname = jo.getString("name");
        User u = User.load("github//" + name);
        if (u == null) {
          V v = V.create("name", "github//" + name);
          v.set("photo", photo);
          v.set("nickname", nickname);
          long id = User.create(v);
          u = User.loadById(id);

          String role = Global.getString("user.role", "N/A");
          Role r1 = Role.loadByName(role);
          if (r != null) {
            u.setRole(r1.getId());
          }

        }

        this.setUser(u);
        this.redirect("/user/go");
        return;
      }
    }

    this.println(r.body);
  }
}
