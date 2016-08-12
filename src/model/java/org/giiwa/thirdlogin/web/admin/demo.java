package org.giiwa.thirdlogin.web.admin;

import org.giiwa.core.bean.Beans;
import org.giiwa.core.bean.X;
import org.giiwa.core.bean.Helper.W;
import org.giiwa.thirdlogin.bean.WxUser;
import org.giiwa.framework.web.Model;
import org.giiwa.framework.web.Path;

import net.sf.json.JSONObject;

public class demo extends Model {

  @Path(login = true, access = "access.config.admin")
  public void onGet() {
    int s = this.getInt("s");
    int n = this.getInt("n", 20, "number.per.page");

    W q = W.create();
    String name = this.getString("name");

    if (!X.isEmpty(name) && path == null) {
      q.and("name", name, W.OP_LIKE);
      this.set("name", name);
    }
    Beans<WxUser> bs = WxUser.load(q, s, n);
    this.set(bs, s, n);

    this.show("/admin/thirdlogin.index.html");
  }

  @Path(path = "delete", login = true, access = "access.config.admin")
  public void delete() {
    String id = this.getString("id");
    WxUser.delete(id);
    JSONObject jo = new JSONObject();
    jo.put(X.STATE, 200);
    this.response(jo);
  }

}
