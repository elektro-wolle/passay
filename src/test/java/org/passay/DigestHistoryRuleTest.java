/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.ArrayList;
import java.util.List;
import org.cryptacular.bean.BCryptHashBean;
import org.cryptacular.bean.EncodingHashBean;
import org.cryptacular.spec.CodecSpec;
import org.cryptacular.spec.DigestSpec;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

/**
 * Unit test for {@link DigestHistoryRule}.
 *
 * @author  Middleware Services
 */
public class DigestHistoryRuleTest extends AbstractRuleTest
{

  /** For testing. */
  private final List<PasswordData.Reference> digestRefs = new ArrayList<>();

  /** For testing. */
  private final List<PasswordData.Reference> saltedDigestRefs = new ArrayList<>();

  /** For testing. */
  private final List<PasswordData.Reference> bcryptDigestRefs = new ArrayList<>();

  /** For testing. */
  private final DigestHistoryRule digestRule = new DigestHistoryRule(
    new EncodingHashBean(new CodecSpec("Base64"), new DigestSpec("SHA1"), 1, false));

  /** For testing. */
  private final DigestHistoryRule saltedDigestRule = new DigestHistoryRule(
    new EncodingHashBean(new CodecSpec("Base64"), new DigestSpec("SHA1"), 1, false));

  /** For testing. */
  private final DigestHistoryRule emptyDigestRule = new DigestHistoryRule(
    new EncodingHashBean(new CodecSpec("Base64"), new DigestSpec("SHA1"), 1, false));

  /** For testing. */
  private final DigestHistoryRule bcryptDigestRule = new DigestHistoryRule(new BCryptHashBean());


  /** Initialize rules for this test. */
  @BeforeClass(groups = {"passtest"})
  public void createRules()
  {
    digestRefs.add(new PasswordData.HistoricalReference("history", "safx/LW8+SsSy/o3PmCNy4VEm5s="));
    digestRefs.add(new PasswordData.HistoricalReference("history", "zurb9DyQ5nooY1la8h86Bh0n1iw="));
    digestRefs.add(new PasswordData.HistoricalReference("history", "bhqabXwE3S8E6xNJfX/d76MFOCs="));

    saltedDigestRefs.add(new PasswordData.HistoricalReference("salted-history", "2DSZvOzGiMnm/Mbxt1M3zNAh7P1GebLG"));
    saltedDigestRefs.add(new PasswordData.HistoricalReference("salted-history", "rv1mF2DuarrF//LPP9+AFJal8bMc9G5z"));
    saltedDigestRefs.add(new PasswordData.HistoricalReference("salted-history", "3lABdWxtWhfGKtXBx4MfiWZ1737KnFuG"));

    bcryptDigestRefs.add(
      new PasswordData.HistoricalReference(
        "bcrypt-history",
        "$2a$5$bvIG6Nmid91Mu9RcmmWZfO5HJIMCT8riNW0hEp8f6/FuA2/mHZFpe"));
  }


  /**
   * @return  Test data.
   */
  @DataProvider(name = "passwords")
  public Object[][] passwords()
  {
    return
      new Object[][] {

        {digestRule, new PasswordData("testuser", "t3stUs3r00", digestRefs), null, },
        {
          digestRule,
          new PasswordData("testuser", "t3stUs3r01", digestRefs),
          codes(HistoryRule.ERROR_CODE),
        },
        {
          digestRule,
          new PasswordData("testuser", "t3stUs3r02", digestRefs),
          codes(HistoryRule.ERROR_CODE),
        },
        {
          digestRule,
          new PasswordData("testuser", "t3stUs3r03", digestRefs),
          codes(HistoryRule.ERROR_CODE),
        },

        {saltedDigestRule, new PasswordData("testuser", "t3stUs3r00", saltedDigestRefs), null, },
        {
          saltedDigestRule,
          new PasswordData("testuser", "t3stUs3r01", saltedDigestRefs),
          codes(HistoryRule.ERROR_CODE),
        },
        {
          saltedDigestRule,
          new PasswordData("testuser", "t3stUs3r02", saltedDigestRefs),
          codes(HistoryRule.ERROR_CODE),
        },
        {
          saltedDigestRule,
          new PasswordData("testuser", "t3stUs3r03", saltedDigestRefs),
          codes(HistoryRule.ERROR_CODE),
        },

        {emptyDigestRule, new PasswordData("testuser", "t3stUs3r00"), null, },
        {emptyDigestRule, new PasswordData("testuser", "t3stUs3r01"), null, },
        {emptyDigestRule, new PasswordData("testuser", "t3stUs3r02"), null, },
        {emptyDigestRule, new PasswordData("testuser", "t3stUs3r03"), null, },

        {bcryptDigestRule, new PasswordData("testuser", "p@$$w0rd"), null, },
        {
          bcryptDigestRule,
          new PasswordData("testuser", "password", bcryptDigestRefs),
          codes(HistoryRule.ERROR_CODE),
        },
      };
  }


  /**
   * @return  Test data.
   */
  @DataProvider(name = "messages")
  public Object[][] messages()
  {
    return
      new Object[][] {
        {
          digestRule,
          new PasswordData("testuser", "t3stUs3r01", digestRefs),
          new String[] {String.format("Password matches one of %s previous passwords.", digestRefs.size()), },
        },
      };
  }
}
