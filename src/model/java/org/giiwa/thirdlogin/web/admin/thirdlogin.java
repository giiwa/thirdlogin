package org.giiwa.thirdlogin.web.admin;

import org.giiwa.core.bean.X;
import org.giiwa.core.bean.Helper.W;
import org.giiwa.framework.web.Model;
import org.giiwa.framework.web.Path;

public class thirdlogin extends Model {

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

    this.show("/admin/thirdlogin.index.html");
  }

}
