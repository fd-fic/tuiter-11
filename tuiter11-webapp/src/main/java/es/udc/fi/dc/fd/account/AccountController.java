package es.udc.fi.dc.fd.account;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import es.udc.fi.dc.fd.block.Block;
import es.udc.fi.dc.fd.block.BlockRepository;
import es.udc.fi.dc.fd.favorite.FavoriteRepository;
import es.udc.fi.dc.fd.follow.Follow;
import es.udc.fi.dc.fd.follow.FollowRepository;
import es.udc.fi.dc.fd.publish.Publish;
import es.udc.fi.dc.fd.publish.PublishService;
import es.udc.fi.dc.fd.publish.PublishUtils;
import es.udc.fi.dc.fd.retuit.RetuitRepository;
import es.udc.fi.dc.fd.tuit.Tuit;

/**
 * The Class AccountController.
 */
@Controller
@Secured("ROLE_USER")
class AccountController {

    /** The Constant USERPROFILE_VIEW. */
    private static final String USERPROFILE_VIEW = "/profile/profile";

    /** The account repository. */
    @Autowired
    private AccountRepository accountRepository;

    /** The retuit repository. */
    @Autowired
    private RetuitRepository retuitRepository;

    /** The follow repository. */
    @Autowired
    private FollowRepository followRepository;

    /** The favorite repository. */
    @Autowired
    private FavoriteRepository favoriteRepository;

    /** The block repository. */
    @Autowired
    private BlockRepository blockRepository;

    /** The publish service. */
    @Autowired
    private PublishService publishService;

    /** The publish utils. */
    @Autowired
    private PublishUtils publishUtils;

    /**
     * Create a default Account Controller.
     */
    public AccountController() {

    }

    // ------------------ MODEL ATRIBUTES --------------------------

    /**
     * Gets the my account.
     *
     * @param principal
     *            the principal
     * @return the my account
     */
    @ModelAttribute("myAccount")
    public Account getMyAccount(Principal principal) {
        return accountRepository.findByEmail(principal.getName());
    }

    /**
     * Gets the profile account.
     *
     * @param nickName
     *            the nick name
     * @return the profile account
     */
    @ModelAttribute("profileAccount")
    public Account getProfileAccount(@PathVariable("nickName") String nickName) {

        return accountRepository.findByNick(nickName);
    }

    /**
     * Gets the time line publications.
     *
     * @param nickName
     *            the nick name
     * @return the time line publications
     */
    @ModelAttribute("publications")
    public List<Publish> getTimeLinePublications(
            @PathVariable("nickName") String nickName) {

        Account nickAccount = accountRepository.findByNick(nickName);

        List<Publish> result = publishService.getUserPublications(nickAccount
                .getEmail());
        return result;
    }

    /**
     * Gets the my retuits.
     *
     * @param principal
     *            the principal
     * @return the my retuits
     */
    @ModelAttribute("myRetuitedTuits")
    public List<Tuit> getMyRetuits(Principal principal) {
        if (principal == null) {
            return null;
        }
        return retuitRepository.findTuitsRetuitedBy(principal.getName());

    }

    /**
     * Gets the my favorite tuits.
     *
     * @param principal
     *            the principal
     * @return the my favorite tuits
     */
    @ModelAttribute("myFavoriteTuits")
    public List<Tuit> getMyFavoriteTuits(Principal principal) {
        if (principal == null) {
            return null;
        }
        return favoriteRepository.findFavoriteTuitsOf(principal.getName());

    }

    /**
     * Checks if is followed by me.
     *
     * @param nickName
     *            the nick name
     * @param principal
     *            the principal
     * @return true, if is followed by me
     */
    @ModelAttribute("isFollowedByMe")
    public boolean isFollowedByMe(@PathVariable("nickName") String nickName,
            Principal principal) {
        Account myProfile = accountRepository.findByEmail(principal.getName());
        Account followedProfile = accountRepository.findByNick(nickName);

        return followRepository.isFollowedByFollower(followedProfile.getId(),
                myProfile.getId());
    }

    /**
     * Checks if is blocked by me.
     *
     * @param nickName
     *            the nick name
     * @param principal
     *            the principal
     * @return true, if is blocked by me
     */
    @ModelAttribute("isBlokedByMe")
    public boolean isBlockedByMe(@PathVariable("nickName") String nickName,
            Principal principal) {
        Account visitedProfile = accountRepository.findByNick(nickName);

        return blockRepository.isBlockedByBlocker(visitedProfile.getEmail(),
                principal.getName());
    }

    /**
     * Im blocked.
     *
     * @param nickName
     *            the nick name
     * @param principal
     *            the principal
     * @return true, if successful
     */
    @ModelAttribute("imBlocked")
    public boolean imBlocked(@PathVariable("nickName") String nickName,
            Principal principal) {
        Account visitedProfile = accountRepository.findByNick(nickName);

        return blockRepository.isBlockedByBlocker(principal.getName(),
                visitedProfile.getEmail());
    }

    /**
     * Gets the followeds.
     *
     * @param receivedNickName
     *            the received nick name
     * @param principal
     *            the principal
     * @return the followeds
     */
    @ModelAttribute("followeds")
    public List<Account> getFolloweds(
            @PathVariable("nickName") String receivedNickName,
            Principal principal) {

        Account profileAccount = accountRepository.findByNick(receivedNickName);
        return followRepository.findFollowedBy(profileAccount.getEmail());
    }

    /**
     * Gets the followers.
     *
     * @param receivedNickName
     *            the received nick name
     * @param principal
     *            the principal
     * @return the followers
     */
    @ModelAttribute("followers")
    public List<Account> getFollowers(
            @PathVariable("nickName") String receivedNickName,
            Principal principal) {

        Account profileAccount = accountRepository.findByNick(receivedNickName);
        return followRepository.findWhoFollowMe(profileAccount.getEmail());
    }

    /**
     * Can see profile.
     *
     * @param receivedNickName
     *            the received nick name
     * @param principal
     *            the principal
     * @return true, if successful
     */
    @ModelAttribute("canIsee")
    public boolean canSeeProfile(
            @PathVariable("nickName") String receivedNickName,
            Principal principal) {

        Account profileAcc = accountRepository.findByNick(receivedNickName);

        Boolean canIsee = publishUtils.canSeeProfile(principal.getName(),
                profileAcc.getEmail());
        return canIsee;
    }

    // ------------------ REQUEST MAPPINGS -------------------------

    /**
     * Accounts.
     *
     * @param principal
     *            the principal
     * @return the account
     */
    @RequestMapping(value = "account/current", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public Account accounts(Principal principal) {
        Assert.notNull(principal);
        return accountRepository.findByEmail(principal.getName());
    }

    /**
     * Allow the users to see the nickName profile if he's not bloked.
     *
     * @param receivedNickName
     *            the received nick name
     * @return the string
     */
    @RequestMapping(value = "/{nickName}", method = RequestMethod.GET)
    public String seeProfile(@PathVariable("nickName") String receivedNickName) {

        return USERPROFILE_VIEW;
    }

    /**
     * Allow the users to follow the nickName profile.
     *
     * @param receivedFollowedNick
     *            the received followed nick
     * @param principal
     *            the principal
     * @return the string
     */
    @RequestMapping(value = "/{nickName}/follow", method = RequestMethod.GET)
    public String follow(@PathVariable("nickName") String receivedFollowedNick,
            Principal principal) {

        Account followedProfile = accountRepository
                .findByNick(receivedFollowedNick);
        Account myProfile = accountRepository.findByEmail(principal.getName());
        followRepository.followUser(myProfile, followedProfile);

        return "redirect:/" + receivedFollowedNick;

    }

    /**
     * Block the user from her profile page.
     *
     * @param receivedBlockedNick
     *            User Nick to block
     * @param principal
     *            the principal
     * @return the string
     */
    @RequestMapping(value = "/{nickName}/block", method = RequestMethod.GET)
    public String block(@PathVariable("nickName") String receivedBlockedNick,
            Principal principal) {

        Account blockedProfile = accountRepository
                .findByNick(receivedBlockedNick);
        Account myProfile = accountRepository.findByEmail(principal.getName());

        blockRepository.blockUser(myProfile, blockedProfile);

        return "redirect:/" + receivedBlockedNick;
    }

    /**
     * UnBlock the user from her profile page.
     *
     * @param receivedBlockedNick
     *            User Nick to block
     * @param principal
     *            the principal
     * @return the string
     */
    @RequestMapping(value = "/{nickName}/unblock", method = RequestMethod.GET)
    public String unBlock(@PathVariable("nickName") String receivedBlockedNick,
            Principal principal) {

        Account blockedProfile = accountRepository
                .findByNick(receivedBlockedNick);

        Block block = blockRepository.findBlockByEmails(principal.getName(),
                blockedProfile.getEmail());
        blockRepository.deleteBlock(block);

        return "redirect:/" + receivedBlockedNick;
    }

    /**
     * Un follow.
     *
     * @param receivedFollowedNick
     *            the received followed nick
     * @param principal
     *            the principal
     * @return the string
     */
    @RequestMapping(value = "/{nickName}/unfollow", method = RequestMethod.GET)
    public String unFollow(
            @PathVariable("nickName") String receivedFollowedNick,
            Principal principal) {

        Account followedProfile = accountRepository
                .findByNick(receivedFollowedNick);
        Account myProfile = accountRepository.findByEmail(principal.getName());

        Follow follow = followRepository.findUserFollowed(myProfile.getId(),
                followedProfile.getId());

        followRepository.removeFollowed(follow);

        return "redirect:/" + receivedFollowedNick;

    }

}
