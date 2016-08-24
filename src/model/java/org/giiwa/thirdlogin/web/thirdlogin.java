package org.giiwa.thirdlogin.web;

import org.giiwa.core.base.Http;
import org.giiwa.core.base.Http.Response;
import org.giiwa.core.bean.Helper.V;
import org.giiwa.core.conf.Global;
import org.giiwa.core.json.JSON;
import org.giiwa.framework.bean.User;
import org.giiwa.framework.web.Model;
import org.giiwa.framework.web.Path;

public class thirdlogin extends Model {

  private static String github_token = "https://github.com/login/oauth/access_token";
  private static String github_user  = "https://api.github.com/user?access_token=%s";

  @Path()
  public void onGet() {

  }

  @Path(path = "github", log = Model.METHOD_GET | Model.METHOD_POST)
  public void github() {
    String code = this.getString("code");
    JSON jo = JSON.create();
    jo.put("client_id", Global.getString("thirdlogin.github.clientid", ""));
    jo.put("client_secret", Global.getString("thirdlogin.github.secret", ""));
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
        }

        this.setUser(u);
        this.redirect("/user/go");
        return;
      }
    }

    this.println(r.body);
  }
}
