package org.giiwa.thirdlogin.bean;

import org.giiwa.core.bean.Bean;
import org.giiwa.core.bean.Beans;
import org.giiwa.core.bean.Table;
import org.giiwa.core.bean.Helper;
import org.giiwa.core.bean.Helper.V;
import org.giiwa.core.bean.Helper.W;
import org.giiwa.core.bean.Column;
import org.giiwa.core.bean.UID;
import org.giiwa.core.bean.X;

/**
 * Demo bean
 * 
 * @author joe
 * 
 */
@Table(name = "tbldemo")
public class WxUser extends Bean {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  @Column(name = X._ID)
  String                    id;

  @Column(name = "name")
  String                    name;

  @Column(name = "content")
  String                    content;

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getContent() {
    return content;
  }

  // ------------

  public static String create(V v) {
    /**
     * generate a unique id in distribute system
     */
    String id = "d" + UID.next("demo.id");
    try {
      while (exists(id)) {
        id = "d" + UID.next("demo.id");
      }
      Helper.insert(v.set(X._ID, id), WxUser.class);
      return id;
    } catch (Exception e1) {
      log.error(e1.getMessage(), e1);
    }
    return null;
  }

  public static boolean exists(String id) {
    try {
      return Helper.exists(id, WxUser.class);
    } catch (Exception e1) {
      log.error(e1.getMessage(), e1);
    }
    return false;
  }

  public static int update(String id, V v) {
    return Helper.update(id, v, WxUser.class);
  }

  public static Beans<WxUser> load(W q, int s, int n) {
    return Helper.load(q.sort(X._ID, 1), s, n, WxUser.class);
  }

  public static WxUser load(String id) {
    return Helper.load(id, WxUser.class);
  }

  public static void delete(String id) {
    Helper.delete(id, WxUser.class);
  }

  public static void cleanup() {
    // TODO Auto-generated method stub
    // just for demo
  }

}
