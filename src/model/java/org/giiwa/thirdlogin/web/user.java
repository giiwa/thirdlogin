package org.giiwa.thirdlogin.web;

import org.giiwa.framework.web.Model;
import org.giiwa.framework.web.Path;

/**
 * web api: /demo
 * 
 * @author joe
 * 
 */
public class user extends Model {

  @Path(path = "thirdlogin")
  public void thirdlogin() {
    // TODO do something
    this.redirect("/user/go");
  }

}
