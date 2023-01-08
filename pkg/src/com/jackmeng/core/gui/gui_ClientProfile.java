
package com.jackmeng.core.gui;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.jackmeng.use_ClientProfile;

public class gui_ClientProfile
    extends gui_HalcyonGenericWindow

{
  private use_ClientProfile profile;
  private gui_HalcyonGenericWindow ge;

  public gui_ClientProfile(use_ClientProfile profile)
  {
    super(new ImageIcon(profile.exposeUserPfp()), "User: " + profile.getUser_Name(), "<html><p><strong>"+profile.getUser_Name()+"'s profile</strong><br>Total Time: " + profile.exposeTotalHours() + " hours", profile);
    this.profile = profile;
  }

  private void __make()
  {
    if (profile != null)
    {

    }
  }

  public use_ClientProfile profile()
  {
    return profile;
  }

  public void profile(use_ClientProfile e)
  {
    this.profile = e;
  }

}
