package com.xinji.demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.amazon.identity.auth.device.authorization.AuthorizationRequest;

import com.pseudos.game.IGameManage;

import com.pseudos.game.base.ads.AdType;
import com.pseudos.game.base.ads.AdsException;
import com.pseudos.game.base.ads.OnAdsListener;
import com.pseudos.game.base.ads.RewardItem;
import com.pseudos.game.base.exception.BaseException;
import com.pseudos.game.base.pay.PayRequest;
import com.pseudos.game.callback.ExitCallBack;
import com.pseudos.game.callback.LoginModeCallBack;
import com.pseudos.game.callback.PayCallBack;
import com.pseudos.game.callback.SubmitUserRoleCallBack;
import com.pseudos.game.callback.InitCallBack;
import com.pseudos.game.entity.GameEventType;
import com.pseudos.game.entity.IUserInfo;
import com.pseudos.game.entity.PlatformType;
import com.pseudos.game.entity.ResultCode;
import com.pseudos.game.exception.LoginException;
import com.pseudos.game.http.data.request.InitRequest;
import com.pseudos.game.http.data.request.GameEventInfoRequest;
import com.pseudos.game.http.data.request.UserGameRoleRequest;
import com.pseudos.game.output.HCDeviceManager;


/**
 * @author chenqm
 * @content 浩凡SDK Demo
 */
public class MainActivity extends Activity {
    private Button btnLoginGG;
    private Button btnLoginFB;
    private Button mBtAuth;                                         // 授权
    private Button mBtLogin;                                        //登录

    private Button btnBind;
    private Button mBtPay;                                          // 立即支付
    private Button mBtSubmit;                                        // 提交角色信息
    private Button mBtLogOut;                                       // 注销登录
    private Button btnShowAd;                                        //展示广告
    //    private String pid = "10014";                                           // PID
//    private String pid = "330189";                                           // PID
//        public String pid="330059";
    public String pid = "310000";//东煌技
    private String introduction;                                  // 推广ID
    public boolean fullScreen = true;                                    // 当前游戏是否为全屏展示(全屏：不带状态栏)
    public String gameName = "安卓测试";
    public String mLoginName;
    public Context mContext;
    public EditText edt_url, edt_pid, edt_gameName, edt_gameId, edt_cent, edt_rmb;
    private Button mBtPayGoogle;
    private String TAG = MainActivity.class.getSimpleName();
    private IUserInfo mCurrentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 模拟当前游戏是否为全屏展示
        if (fullScreen) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_main);
        //初始化控件
        initView();
    }

    private void initView() {
        btnLoginGG = findViewById(R.id.bt_google_login);
        btnLoginFB = findViewById(R.id.bt_fb_login);
        mBtAuth = findViewById(R.id.bt_auth);
        mBtLogin = findViewById(R.id.bt_login);
        btnBind =findViewById(R.id.bt_bind);
        mBtPay = findViewById(R.id.bt_pay);
        mBtSubmit = findViewById(R.id.bt_submit);
        mBtLogOut = findViewById(R.id.bt_logout);
        edt_url = findViewById(R.id.edt_url);
        edt_pid = findViewById(R.id.edt_pid);
        edt_gameName = findViewById(R.id.edt_gameName);
        edt_gameId = findViewById(R.id.edt_gameId);
        edt_cent = findViewById(R.id.edt_cent);
        edt_rmb = findViewById(R.id.edt_rmb);
        mBtPayGoogle = findViewById(R.id.bt_pay_google);
        btnShowAd = findViewById(R.id.bt_show_ad);
        // 授权
        mBtAuth.setOnClickListener(v -> doAuth());
        //登录
        mBtLogin.setOnClickListener(v -> doLogin());
        //绑定
        btnBind.setOnClickListener(v-> doBind());
        // 创建订单信息
        mBtPay.setOnClickListener(v -> doPay());
        // 提交角色信息
        mBtSubmit.setOnClickListener(v -> doSubmitRole());
        //退出登录
        mBtLogOut.setOnClickListener(v -> {
            IGameManage.Builder.build().logout();
            mBtLogin.setEnabled(true);
        });
        //显示广告
        btnShowAd.setOnClickListener(v -> IGameManage.Builder.build().showAds(MainActivity.this, AdType.AD_TYPE_REWARD_VIDEO, "", null));
        //获取设备id
        HCDeviceManager.getInstance().getDeviceId();
    }

    /**
     * 绑定
     */
    private void doBind() {
        IGameManage.Builder.build().bindThirdPart(this);
    }


    /**
     * 开始支付
     */
    private void doPay() {
        String rechargePrice = ""; //订单金额
        String companyOrderNo = System.currentTimeMillis() + ""; //订单ID -唯一性
        String gamersRole = "骷髅小怪"; //角色名
        String productName = "测试订单"; //商品名称
        String serverNum = "1";//区服ID
        String serverName = "1区";//区服名称
        String grade = "1";//游戏等级
        String resourceID = "com.hkhfgame.mmamx_6";
        String extra = "无";
        String amountRmb = "";//补充参数:人民币金额
        String cent = edt_cent.getText().toString();
        String rmb = edt_rmb.getText().toString();
        if (!TextUtils.isEmpty(cent)) {
            rechargePrice = cent;
        } else {

            return;
        }
        if (!TextUtils.isEmpty(rmb)) {
            amountRmb = rmb;
        } else {
            return;
        }

        PayRequest request = new PayRequest();
        request.setAmount(Integer.valueOf(rechargePrice));
        request.setRmbAmount(Integer.parseInt(amountRmb));
        //todo 原始价格，用于打折原价等需求时传递，没有折扣的可不传
        request.setOriginalAmount(Integer.valueOf(rechargePrice));
        request.setCompanyOrderNo(companyOrderNo);
        request.setGamersRole(gamersRole);
        request.setProductName(productName);
        request.setServerName(serverName);
        request.setServerNum(serverNum);
        request.setGrade(grade);
        request.setProductId(resourceID);
        request.setExtra(extra);
        request.setUserNo(mCurrentUser.getXJUserNo());
        IGameManage.Builder.build().pay(MainActivity.this, request, new PayCallBack() {
            @Override
            public void onSuccess() {
                Toast.makeText(MainActivity.this, "支付成功", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailed(BaseException e) {
                Toast.makeText(MainActivity.this, "支付失败：" + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "支付取消", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 登录
     */
    private void doLogin() {
        IGameManage.Builder.build().login(MainActivity.this, new LoginModeCallBack() {
            @Override
            public void onLoginModeSuccess(IUserInfo info) {
                Log.e(TAG, "登录成功" + info.getXJLoginName() + " 是否绑定邮箱：" + info.hasBindEmail());
//                        mBtLogin.setEnabled(false);
                mLoginName = info.getXJLoginName();
                Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                mCurrentUser = info;
            }

            @Override
            public void onLoginModeFail(LoginException e) {
                Toast.makeText(MainActivity.this, "登录失败：" + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLoginModeCancel() {
                Log.e(TAG, "登录取消");
            }
        });
    }

    /**
     * 提交角色
     */
    private void doSubmitRole() {
        LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.layout_submit_role, null);
        final EditText etUserNo = contentView.findViewById(R.id.et_user_no);
        final EditText etLoginName = contentView.findViewById(R.id.et_login_name);
        final EditText etGamersGrade = contentView.findViewById(R.id.et_gamers_grade);
        final EditText etGamersRoleId = contentView.findViewById(R.id.et_gamers_role_id);
        final EditText etGamersRoleName = contentView.findViewById(R.id.et_gamers_role_name);
        final EditText etGameId = contentView.findViewById(R.id.et_game_id);
        final EditText etGameName = contentView.findViewById(R.id.et_game_name);
        final EditText etServerNum = contentView.findViewById(R.id.et_server_num);
        final EditText etServerName = contentView.findViewById(R.id.et_server_name);

        String grade = "1";//用户等级
        String gamersRoleId = "123";// 角色ID
        String gamersRole = "训练家0009";  // 角色名称
        String serverNum = "1区";// 服务器名称
        etServerNum.setText(serverNum);
        etLoginName.setText(gamersRole);
        etGameId.setText(pid);
        etGameName.setText(gameName);
        new AlertDialog.Builder(MainActivity.this)
                .setPositiveButton("提交角色",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final String gameGrade = etGamersGrade.getText().toString().trim();
                                if (TextUtils.isEmpty(gameGrade)) {
                                    return;
                                }
                                String gameRoleId = etGamersRoleId.getText().toString().trim();
                                if (TextUtils.isEmpty(gameRoleId)) {
                                    return;
                                }
                                String gameRoleName = etGamersRoleName.getText().toString().trim();
                                if (TextUtils.isEmpty(gameRoleName)) {
                                    return;
                                }

                                GameEventInfoRequest request = new GameEventInfoRequest();
                                request.setLoginName(mLoginName);
                                request.setGamersGrade(gameGrade);
                                request.setGamersRole(gameRoleName);
                                request.setGamersRoleId(gameRoleId);
                                request.setServerNum("1001");
                                request.setServerName("妙蛙種子");
                                request.setGameName(gameName);
                                //角色提交接口，登录后和角色升级时调用
                                IGameManage.Builder.build().submitGameEventInfo(MainActivity.this, GameEventType.GAME_EVENT_TYPE_LEVEL, request, new SubmitUserRoleCallBack() {
                                    @Override
                                    public void onSubmitSuccess() {
                                        Log.e(TAG, "提交成功");
                                    }

                                    @Override
                                    public void onSubmitFailed() {
                                        Log.e(TAG, "提交失败");
                                    }
                                });
                            }
                        })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setView(contentView)
                .show();
    }

    /**
     * 授权
     */
    private void doAuth() {
        //区域
        RadioGroup rg = findViewById(R.id.rg_platform);
        int platformID = rg.getCheckedRadioButtonId();
        int platformType = PlatformType.XJ_TYPE_PLATFORM_HK.getType();
        if (platformID == R.id.rb_hk) {
            platformType = PlatformType.XJ_TYPE_PLATFORM_HK.getType();
        } else if (platformID == R.id.rb_us) {
            platformType = PlatformType.XJ_TYPE_PLATFORM_US.getType();
        } else if (platformID == R.id.rb_ru) {
            platformType = PlatformType.XJ_TYPE_PLATFORM_RU.getType();
        }

        String pidStr = edt_pid.getText().toString().trim();
        if (!TextUtils.isEmpty(pidStr)) {
            pid = pidStr;
        }
        String gameNameStr = edt_gameName.getText().toString().trim();
        if (!TextUtils.isEmpty(gameNameStr)) {
            gameName = gameNameStr;
        }

        // 对应用进行授权,若无授权或者是授权失败,将导致无法使用本支付SDK
        InitRequest request = new InitRequest();
        request.setFullScreen(fullScreen + "");
        request.setPid(pid);
        request.setGameName(gameName);
        //todo 根据运营提供的 平台类型来具体填充
        request.setPlatformType(PlatformType.XJ_TYPE_PLATFORM_HK.getType());
        IGameManage.Builder.build().init(MainActivity.this, request, new InitCallBack() {
            @Override
            public void onLogout() {
                //处理退出登录回调:一般处理逻辑为退出游戏，并调用登录接口
                //todo 游戏退出逻辑
                doLogin();
            }
            @Override
            public void onBindThirdPart(IUserInfo ixjUserInfo) {
                //游客转普通登录，绑定三方登录成功
            }
            @Override
            public void onCancellationAccount() {
                //注销账号成功：处理逻辑跟退出账号即可
                doLogin();
            }
            @Override
            public void onInitResult(int code, String s) {
                //授权成功后再调用登录接口
                if (code == ResultCode.SUC.getResultCode()) {
                    Log.e(TAG, "授权成功");
                    mBtAuth.setEnabled(false);
                    Toast.makeText(MainActivity.this, "授权成功", Toast.LENGTH_LONG).show();
                    registerAds();
                } else {
                    Log.e(TAG, "授权失败Main");
                }
            }

            @Override
            public void onTrackEvents(String s, String s1) {

            }
        });
    }


    /**
     * 注册广告
     */
    private void registerAds() {
        IGameManage.Builder.build().registerAds(this, AdType.AD_TYPE_REWARD_VIDEO, "", new OnAdsListener() {
            @Override
            public void onLoadSuc(RewardItem item) {
                Log.d("XJ_AD", "加载广告成功");
            }

            @Override
            public void onLoadFailed(AdsException e) {
                Log.e("XJ_AD", e.getMessage());
            }

            @Override
            public void onDisplaySuc(RewardItem item) {
                Log.d("XJ_AD", "展示广告成功，价格：" + item.amount);
            }

            @Override
            public void onDisplayFailed(AdsException e) {
                Log.e("XJ_AD", e.getMessage());
            }

            @Override
            public void onAdReward(RewardItem xjRewardItem) {
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        IGameManage.Builder.build().onActivityResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IGameManage.Builder.build().onActivityDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            IGameManage.Builder.build().exit(this, () -> System.out.println("游戏退出了"));
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            return true;

        }
        return super.onKeyDown(keyCode, event);
    }


}
