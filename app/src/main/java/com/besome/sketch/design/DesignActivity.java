package com.besome.sketch.design;

import static mod.SketchwareUtil.getDip;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.besome.sketch.beans.ProjectFileBean;
import com.besome.sketch.common.SrcViewerActivity;
import com.besome.sketch.editor.manage.ManageCollectionActivity;
import com.besome.sketch.editor.manage.font.ManageFontActivity;
import com.besome.sketch.editor.manage.image.ManageImageActivity;
import com.besome.sketch.editor.manage.library.ManageLibraryActivity;
import com.besome.sketch.editor.manage.sound.ManageSoundActivity;
import com.besome.sketch.editor.manage.view.ManageViewActivity;
import com.besome.sketch.editor.view.ProjectFileSelector;
import com.besome.sketch.lib.base.BaseAppCompatActivity;
import com.besome.sketch.lib.ui.CustomViewPager;
import com.besome.sketch.tools.CompileLogActivity;
import com.besome.sketch.tools.ExportApkActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.sketchware.remod.Resources;

import java.io.File;
import java.util.HashMap;

import a.a.a.DB;
import a.a.a.Dp;
import a.a.a.Ep;
import a.a.a.GB;
import a.a.a.MA;
import a.a.a.Xf;
import a.a.a.aB;
import a.a.a.bB;
import a.a.a.bC;
import a.a.a.br;
import a.a.a.cC;
import a.a.a.gg;
import a.a.a.jC;
import a.a.a.jr;
import a.a.a.kC;
import a.a.a.lC;
import a.a.a.mB;
import a.a.a.rs;
import a.a.a.uo;
import a.a.a.wq;
import a.a.a.xB;
import a.a.a.yB;
import a.a.a.yq;
import dev.aldi.sayuti.editor.manage.ManageCustomAttributeActivity;
import dev.aldi.sayuti.editor.manage.ManageLocalLibraryActivity;
import io.github.rosemoe.sora.langs.java.JavaLanguage;
import io.github.rosemoe.sora.widget.CodeEditor;
import io.github.rosemoe.sora.widget.schemes.EditorColorScheme;
import mod.SketchwareUtil;
import mod.agus.jcoderz.editor.manage.permission.ManagePermissionActivity;
import mod.agus.jcoderz.editor.manage.resource.ManageResourceActivity;
import mod.agus.jcoderz.lib.FileUtil;
import mod.alucard.tn.apksigner.ApkSigner;
import mod.hey.studios.activity.managers.assets.ManageAssetsActivity;
import mod.hey.studios.activity.managers.java.ManageJavaActivity;
import mod.hey.studios.activity.managers.nativelib.ManageNativelibsActivity;
import mod.hey.studios.build.BuildSettings;
import mod.hey.studios.build.BuildSettingsDialog;
import mod.hey.studios.project.DesignActRunnable;
import mod.hey.studios.project.custom_blocks.CustomBlocksDialog;
import mod.hey.studios.project.proguard.ManageProguardActivity;
import mod.hey.studios.project.proguard.ProguardHandler;
import mod.hey.studios.project.stringfog.ManageStringfogActivity;
import mod.hey.studios.project.stringfog.StringfogHandler;
import mod.hey.studios.util.Helper;
import mod.hilal.saif.activities.android_manifest.AndroidManifestInjection;
import mod.hosni.fraj.compilerlog.CompileErrorSaver;
import mod.jbk.util.LogUtil;
import mod.tyron.compiler.Compiler;
import mod.tyron.compiler.IncrementalCompiler;

public class DesignActivity extends BaseAppCompatActivity implements OnClickListener, uo {

    private ImageView xmlLayoutOrientation;
    private boolean B = false;
    private int currentTabNumber;
    private DesignActivity.f J = null;
    private String sc_id;
    private CustomViewPager viewPager;
    private CoordinatorLayout coordinatorLayout;
    private DrawerLayout drawer;
    private yq q;
    private DB r;
    private DB t;
    /**
     * The Run-Button in bottom right corner
     */
    private Button runProject;
    private ProjectFileSelector projectFileSelector;
    private jr viewTabAdapter = null;
    private rs eventTabAdapter = null;
    private br componentTabAdapter = null;

    /**
     * Saves the app's version information to the currently opened Sketchware project file.
     */
    private void A() {
        HashMap<String, Object> projectMetadata = lC.b(sc_id);
        if (projectMetadata != null) {
            projectMetadata.put("sketchware_ver", GB.d(getApplicationContext()));
            lC.b(sc_id, projectMetadata);
        }
    }

    private void a(boolean var1) {
        jC.a(sc_id, var1);
        jC.b(sc_id, var1);
        kC var2 = jC.d(sc_id, var1);
        jC.c(sc_id, var1);
        cC.c(sc_id);
        bC.d(sc_id);
        if (!var1) {
            var2.f();
            var2.g();
            var2.e();
        }
    }

    /**
     * Sets the activity's title in the Toolbar.
     */
    private void b(String newTitle) {
        d().a(newTitle);
    }

    public void b(boolean var1) {
        if (var1) {
            viewPager.l();
        } else {
            viewPager.k();
        }
    }

    /**
     * Shows a Snackbar indicating that a problem occurred while compiling. The user can click on "SHOW" to get to {@link CompileLogActivity}.
     *
     * @param error The error, to be later displayed as text in {@link CompileLogActivity}
     */
    private void d(String error) {
        new CompileErrorSaver(q.b).writeLogsToFile(error);
        Snackbar snackbar = Snackbar.a(coordinatorLayout, "Show compile log", -2 /* BaseTransientBottomBar.LENGTH_INDEFINITE */);
        snackbar.a(xB.b().a(getApplicationContext(), Resources.string.common_word_show), v -> {
            if (!mB.a()) {
                snackbar.c();
                Intent intent = new Intent(getApplicationContext(), CompileLogActivity.class);
                intent.putExtra("error", error);
                intent.putExtra("sc_id", sc_id);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        /* Set the text color to yellow */
        snackbar.f(Color.YELLOW);
        snackbar.n();
    }

    @Override
    public void e(int i) {
        if (i == 188) {
            new a(getApplicationContext()).execute();
        }
    }

    @Override
    public void finish() {
        jC.a();
        cC.a();
        bC.a();
        setResult(RESULT_CANCELED, getIntent());
        super.finish();
    }

    private void l() {
        if (jC.c(sc_id).g() || jC.b(sc_id).g() || jC.d(sc_id).q() || jC.a(sc_id).d() || jC.a(sc_id).c()) {
            s();
        }
    }

    private void n() {
        q.b(jC.b(sc_id), jC.a(sc_id), jC.c(sc_id));
    }

    /**
     * Opens the debug APK to install.
     */
    private void o() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= 24) {
            Uri apkUri = FileProvider.a(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", new File(q.H));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(new File(q.H)), "application/vnd.android.package-archive");
        }

        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 188:
                new a(getApplicationContext()).execute();
                break;

            case 208:
                if (resultCode == RESULT_OK) {
                    if (projectFileSelector != null) {
                        projectFileSelector.a();
                    }
                    if (viewTabAdapter != null) {
                        viewTabAdapter.n();
                    }
                }
                break;

            case 209:
                if (resultCode == RESULT_OK) {
                    if (viewTabAdapter != null) {
                        viewTabAdapter.i();
                    }
                }
                break;

            case 217:
            case 228:
                break;

            case 223:
                if (resultCode == RESULT_OK) {
                    if (eventTabAdapter != null) {
                        eventTabAdapter.f();
                    }
                }
                break;

            case 224:
                if (resultCode == RESULT_OK) {
                    if (componentTabAdapter != null) {
                        componentTabAdapter.d();
                    }
                }
                break;

            case 226:
                if (resultCode == RESULT_OK) {
                    if (projectFileSelector != null) {
                        projectFileSelector.a();
                    }
                }
                break;

            case 233:
                if (resultCode == RESULT_OK) {
                    viewTabAdapter.j();
                }
                break;

            case 263:
                if (resultCode == RESULT_OK) {
                    projectFileSelector.setXmlFileName(data.getParcelableExtra("project_file"));
                }
                break;

            case 462:
                if (resultCode == RESULT_OK && data.getBooleanExtra("req_update_design_activity", false)) {
                    viewTabAdapter.j();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.f(Gravity.END)) {
            drawer.a(Gravity.END);
        } else if (viewTabAdapter.g()) {
            viewTabAdapter.a(false);
        } else {
            if (currentTabNumber > 0) {
                currentTabNumber--;
                viewPager.setCurrentItem(currentTabNumber);
            } else if (t.c("P12I2")) {
                k();
                new e(getApplicationContext()).execute();
            } else {
                q();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (!mB.a()) {
            if (v.getId() == Resources.id.btn_execute) {
                new DesignActivity.a(getApplicationContext()).execute();
            } else if (v.getId() == Resources.id.btn_compiler_opt) {
                PopupMenu popupMenu = new PopupMenu(this, findViewById(Resources.id.btn_compiler_opt));
                Menu menu = popupMenu.getMenu();

                // TODO: Add nice title item(s) which are smaller, can't be selected, etc.
                menu.add(Menu.NONE, 1, Menu.NONE, "Build Settings");
                menu.add(Menu.NONE, 2, Menu.NONE, "Clean temporary files");
                menu.add(Menu.NONE, 3, Menu.NONE, "Show last compile error");
                menu.add(Menu.NONE, 5, Menu.NONE, "Show source code");
                if (FileUtil.isExistFile(q.H)) {
                    menu.add(Menu.NONE, 4, Menu.NONE, "Install last built APK");
                }

                popupMenu.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case 1:
                            new BuildSettingsDialog(DesignActivity.this, sc_id).show();
                            break;

                        case 2:
                            new Thread() {
                                @Override
                                public void run() {
                                    FileUtil.deleteFile(q.c);
                                    runOnUiThread(() ->
                                            SketchwareUtil.toast("Done cleaning temporary files!"));
                                }
                            }.start();
                            break;

                        case 3:
                            new CompileErrorSaver(sc_id).showLastErrors(DesignActivity.this);
                            break;

                        case 4:
                            if (FileUtil.isExistFile(q.H)) {
                                o();
                            } else {
                                SketchwareUtil.toast("APK doesn't exist anymore");
                            }
                            break;

                        case 5:
                            showCurrentActivitySrcCode();
                            break;

                        default:
                            return false;
                    }

                    return true;
                });

                popupMenu.show();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(Resources.layout.design);
        if (!j()) {
            finish();
        }

        if (savedInstanceState == null) {
            sc_id = getIntent().getStringExtra("sc_id");
        } else {
            sc_id = savedInstanceState.getString("sc_id");
        }

        r = new DB(getApplicationContext(), "P1");
        t = new DB(getApplicationContext(), "P12");

        Toolbar toolbar = findViewById(Resources.id.toolbar);
        toolbar.setSubtitle(sc_id);
        a(toolbar);
        findViewById(Resources.id.layout_main_logo).setVisibility(View.GONE);
        d().d(true);
        d().e(true);
        toolbar.setNavigationOnClickListener(Helper.getBackPressedClickListener(this));
        toolbar.setPopupTheme(Resources.style.ThemeOverlay_ToolbarMenu);

        // Replaced empty anonymous class with null
        getSupportFragmentManager().a((Xf.c) null);
        drawer = findViewById(Resources.id.drawer_layout);
        drawer.setDrawerLockMode(1 /* DrawerLayout#LOCK_MODE_LOCKED_CLOSED */);
        coordinatorLayout = findViewById(Resources.id.layout_coordinator);
        runProject = findViewById(Resources.id.btn_execute);
        runProject.setText(xB.b().a(this, Resources.string.common_word_run));
        runProject.setOnClickListener(this);
        findViewById(Resources.id.btn_compiler_opt).setOnClickListener(this);
        xmlLayoutOrientation = findViewById(Resources.id.img_orientation);
        projectFileSelector = findViewById(Resources.id.file_selector);
        projectFileSelector.setScId(sc_id);
        projectFileSelector.setOnSelectedFileChangeListener((i, projectFileBean) -> {
            if (i == 0) {
                if (viewTabAdapter != null && projectFileBean != null) {
                    int orientation = projectFileBean.orientation;
                    if (orientation == ProjectFileBean.ORIENTATION_PORTRAIT) {
                        xmlLayoutOrientation.setImageResource(Resources.drawable.ic_screen_portrait_grey600_24dp);
                    } else if (orientation == ProjectFileBean.ORIENTATION_LANDSCAPE) {
                        xmlLayoutOrientation.setImageResource(Resources.drawable.ic_screen_landscape_grey600_24dp);
                    } else {
                        xmlLayoutOrientation.setImageResource(Resources.drawable.ic_screen_rotation_grey600_24dp);
                    }
                    viewTabAdapter.a(projectFileBean);
                }
            } else if (i == 1) {
                if (eventTabAdapter != null) {
                    if (projectFileBean != null) {
                        eventTabAdapter.a(projectFileBean);
                        eventTabAdapter.f();
                    } else {
                        return;
                    }
                }
                if (componentTabAdapter != null && projectFileBean != null) {
                    componentTabAdapter.a(projectFileBean);
                    componentTabAdapter.d();
                }
            }
        });
        viewPager = findViewById(Resources.id.viewpager);
        viewPager.setAdapter(new DesignActivity.g(getSupportFragmentManager(), this));
        viewPager.setOffscreenPageLimit(3);
        viewPager.a(new ViewPager.e() {

            @Override
            public void a(int i) {
            }

            @Override
            public void a(int i, float v, int i1) {
            }

            @Override
            public void b(int i) {
                if (currentTabNumber == 1) {
                    if (eventTabAdapter != null) {
                        eventTabAdapter.c();
                    }
                } else if (currentTabNumber == 2 && componentTabAdapter != null) {
                    componentTabAdapter.c();
                }
                if (i == 0) {
                    if (viewTabAdapter != null) {
                        viewTabAdapter.c(true);
                        xmlLayoutOrientation.setVisibility(View.VISIBLE);
                        projectFileSelector.setFileType(0);
                        projectFileSelector.a();
                    }
                } else if (i == 1) {
                    if (viewTabAdapter != null) {
                        xmlLayoutOrientation.setVisibility(View.GONE);
                        viewTabAdapter.c(false);
                        projectFileSelector.setFileType(1);
                        projectFileSelector.a();
                        if (eventTabAdapter != null) {
                            eventTabAdapter.f();
                        }
                    }
                } else {
                    if (viewTabAdapter != null) {
                        viewTabAdapter.c(false);
                        xmlLayoutOrientation.setVisibility(View.GONE);
                        projectFileSelector.setFileType(1);
                        projectFileSelector.a();
                        if (componentTabAdapter != null) {
                            componentTabAdapter.d();
                        }
                    }
                }
                currentTabNumber = i;
            }
        });
        viewPager.getAdapter().b();
        ((TabLayout) findViewById(Resources.id.tab_layout)).setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(Resources.menu.design_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId != Resources.id.design_actionbar_titleopen_drawer) {
            if (itemId == Resources.id.design_option_menu_title_save_project) {
                k();
                new d(getApplicationContext()).execute();
            }
        } else if (!drawer.f(Gravity.END)) {
            drawer.h(Gravity.END);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        k();

        HashMap<String, Object> projectInfo = lC.b(sc_id);
        b(yB.c(projectInfo, "my_ws_name"));
        q = new yq(getApplicationContext(), wq.d(sc_id), projectInfo);

        try {
            new b(getBaseContext(), savedInstanceState).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!j()) {
            finish();
        }

        long freeMegabytes = GB.c();
        if (freeMegabytes < 100L && freeMegabytes > 0L) {
            r();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("sc_id", sc_id);
        projectFileSelector.b(outState);
        super.onSaveInstanceState(outState);
        if (!j()) {
            finish();
        }

        if (J != null && !J.isCancelled()) {
            J.cancel(true);
        }

        if (!B) {
            J = new DesignActivity.f(getApplicationContext());
            J.execute();
        }
    }

    /**
     * Show a dialog asking about saving the project before quitting.
     */
    private void q() {
        aB dialog = new aB(this);
        dialog.b(xB.b().a(getApplicationContext(), Resources.string.design_quit_title_exit_projet));
        dialog.a(Resources.drawable.exit_96);
        dialog.a(xB.b().a(getApplicationContext(), Resources.string.design_quit_message_confirm_save));
        dialog.b(xB.b().a(getApplicationContext(), Resources.string.design_quit_button_save_and_exit), v -> {
            if (!mB.a()) {
                dialog.dismiss();
                try {
                    k();
                    new e(getApplicationContext()).execute();
                } catch (Exception e) {
                    e.printStackTrace();
                    h();
                }
            }
        });
        dialog.a(xB.b().a(getApplicationContext(), Resources.string.common_word_exit), v -> {
            if (!mB.a()) {
                dialog.dismiss();
                try {
                    k();
                    new c(getApplicationContext()).execute();
                } catch (Exception e) {
                    e.printStackTrace();
                    h();
                }
            }
        });
        dialog.configureDefaultButton(xB.b().a(getApplicationContext(), Resources.string.common_word_cancel),
                Helper.getDialogDismissListener(dialog));
        dialog.show();
    }

    /**
     * Show a dialog warning the user about low free space.
     */
    private void r() {
        aB dialog = new aB(this);
        dialog.b(xB.b().a(getApplicationContext(), Resources.string.common_word_warning));
        dialog.a(Resources.drawable.break_warning_96_red);
        dialog.a(xB.b().a(getApplicationContext(), Resources.string.common_message_insufficient_storage_space));
        dialog.b(xB.b().a(getApplicationContext(), Resources.string.common_word_ok),
                Helper.getDialogDismissListener(dialog));
        dialog.show();
    }

    private void s() {
        B = true;
        aB dialog = new aB(this);
        dialog.a(Resources.drawable.data_backup_96);
        dialog.b(xB.b().a(getApplicationContext(), Resources.string.design_restore_data_title));
        dialog.a(xB.b().a(getApplicationContext(), Resources.string.design_restore_data_message_confirm));
        dialog.b(xB.b().a(getApplicationContext(), Resources.string.common_word_restore), v -> {
            if (!mB.a()) {
                boolean g = jC.c(sc_id).g();
                boolean g2 = jC.b(sc_id).g();
                boolean q = jC.d(sc_id).q();
                boolean d = jC.a(sc_id).d();
                boolean c = jC.a(sc_id).c();
                if (g) {
                    jC.c(sc_id).h();
                }
                if (g2) {
                    jC.b(sc_id).h();
                }
                if (q) {
                    jC.d(sc_id).r();
                }
                if (d) {
                    jC.a(sc_id).h();
                }
                if (c) {
                    jC.a(sc_id).f();
                }
                if (g) {
                    jC.b(sc_id).a(jC.c(sc_id));
                    jC.a(sc_id).a(jC.c(sc_id).d());
                }
                if (g2 || g) {
                    jC.a(sc_id).a(jC.b(sc_id));
                }
                if (q) {
                    jC.a(sc_id).b(jC.d(sc_id));
                    jC.a(sc_id).c(jC.d(sc_id));
                    jC.a(sc_id).a(jC.d(sc_id));
                }
                projectFileSelector.a();
                B = false;
                dialog.dismiss();
            }
        });
        dialog.a(xB.b().a(getApplicationContext(), Resources.string.common_word_no), v -> {
            B = false;
            dialog.dismiss();
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    private void showCurrentActivitySrcCode() {
        ProgressDialog progress = new ProgressDialog(DesignActivity.this);
        progress.setMessage("Generating source...");
        progress.show();

        new Thread(() -> {
            final String source = new yq(getApplicationContext(), sc_id).getFileSrc(projectFileSelector.getFileName(), jC.b(sc_id), jC.a(sc_id), jC.c(sc_id));

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DesignActivity.this)
                    .setTitle(projectFileSelector.getFileName())
                    .setPositiveButton("Dismiss", null);

            runOnUiThread(() -> {
                progress.dismiss();

                CodeEditor editor = new CodeEditor(DesignActivity.this);
                editor.setTypefaceText(Typeface.MONOSPACE);
                editor.setEditable(false);
                editor.setEditorLanguage(new JavaLanguage());
                editor.setColorScheme(new EditorColorScheme());
                editor.setTextSize(14);
                editor.setText(!source.equals("") ? source : "Failed to generate source.");

                AlertDialog dialog = dialogBuilder.create();
                dialog.setView(editor,
                        (int) getDip(24),
                        (int) getDip(8),
                        (int) getDip(24),
                        (int) getDip(8));
                dialog.show();
            });
        }).start();
    }

    /**
     * Opens {@link ManageCollectionActivity}.
     */
    public void t() {
        Intent intent = new Intent(getApplicationContext(), ManageCollectionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("sc_id", sc_id);
        startActivityForResult(intent, 233);
    }

    /**
     * Opens {@link AndroidManifestInjection}.
     */
    public void toAndroidManifest() {
        Intent intent = new Intent(getApplicationContext(), AndroidManifestInjection.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("sc_id", sc_id);
        intent.putExtra("file_name", projectFileSelector.g);
        startActivity(intent);
    }

    /**
     * Opens {@link ManageCustomAttributeActivity}.
     */
    public void toAppCompatInjection() {
        Intent intent = new Intent(getApplicationContext(), ManageCustomAttributeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("sc_id", sc_id);
        intent.putExtra("file_name", projectFileSelector.f);
        startActivity(intent);
    }

    /**
     * Opens {@link ManageAssetsActivity}.
     */
    public void toAssets() {
        Intent intent = new Intent(getApplicationContext(), ManageAssetsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("sc_id", sc_id);
        startActivity(intent);
    }

    /**
     * Shows a {@link CustomBlocksDialog}.
     */
    public void toCustomBlocks() {
        CustomBlocksDialog.show(this, sc_id);
    }

    /**
     * Opens {@link ExportApkActivity}.
     */
    public void toExportApk() {
        Intent intent = new Intent(getApplicationContext(), ExportApkActivity.class);
        intent.putExtra("scId", sc_id);
        startActivity(intent);
    }

    /**
     * Opens {@link ManageJavaActivity}.
     */
    public void toJava() {
        Intent intent = new Intent(getApplicationContext(), ManageJavaActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("sc_id", sc_id);
        intent.putExtra("pkgName", q.e);
        startActivity(intent);
    }

    /**
     * Opens {@link ManageLocalLibraryActivity}.
     */
    public void toLocalLibrary() {
        Intent intent = new Intent(getApplicationContext(), ManageLocalLibraryActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("sc_id", sc_id);
        startActivity(intent);
    }

    /**
     * Opens {@link ManageNativelibsActivity}.
     */
    public void toNativelibs() {
        Intent intent = new Intent(getApplicationContext(), ManageNativelibsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("sc_id", sc_id);
        startActivity(intent);
    }

    /**
     * Opens {@link ManagePermissionActivity}.
     */
    public void toPermission() {
        Intent intent = new Intent(getApplicationContext(), ManagePermissionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("sc_id", sc_id);
        startActivity(intent);
    }

    /**
     * Opens {@link ManageProguardActivity}.
     */
    public void toProguard() {
        Intent intent = new Intent(getApplicationContext(), ManageProguardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("sc_id", sc_id);
        startActivity(intent);
    }

    /**
     * Opens {@link ManageResourceActivity}.
     */
    public void toResource() {
        Intent intent = new Intent(getApplicationContext(), ManageResourceActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("sc_id", sc_id);
        startActivity(intent);
    }

    /**
     * Opens {@link ManageStringfogActivity}.
     */
    public void toStringfog() {
        Intent intent = new Intent(getApplicationContext(), ManageStringfogActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("sc_id", sc_id);
        startActivity(intent);
    }

    /**
     * Opens {@link ManageFontActivity}.
     */
    public void u() {
        Intent intent = new Intent(getApplicationContext(), ManageFontActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("sc_id", sc_id);
        startActivityForResult(intent, 228);
    }

    /**
     * Opens {@link ManageImageActivity}.
     */
    public void v() {
        Intent intent = new Intent(getApplicationContext(), ManageImageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("sc_id", sc_id);
        startActivityForResult(intent, 209);
    }

    /**
     * Opens {@link ManageLibraryActivity}.
     */
    public void w() {
        Intent intent = new Intent(getApplicationContext(), ManageLibraryActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("sc_id", sc_id);
        startActivityForResult(intent, 226);
    }

    /**
     * Opens {@link ManageViewActivity}.
     */
    public void x() {
        Intent intent = new Intent(getApplicationContext(), ManageViewActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("sc_id", sc_id);
        startActivityForResult(intent, 208);
    }

    /**
     * Opens {@link ManageSoundActivity}.
     */
    public void y() {
        Intent intent = new Intent(getApplicationContext(), ManageSoundActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("sc_id", sc_id);
        startActivityForResult(intent, 217);
    }

    /**
     * Opens {@link SrcViewerActivity}.
     */
    public void z() {
        Intent intent = new Intent(getApplicationContext(), SrcViewerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("sc_id", sc_id);
        if (viewPager.getCurrentItem() == 0) {
            try {
                intent.putExtra("current", viewTabAdapter.d().getXmlName());
            } catch (Exception ignored) {
            }
        } else if (viewPager.getCurrentItem() != 1) {
            intent.putExtra("current", "");
        } else {
            try {
                intent.putExtra("current", eventTabAdapter.d().getJavaName());
            } catch (Exception ignored) {
            }
        }

        startActivityForResult(intent, 240);
    }

    public class a extends MA implements OnCancelListener {

        /**
         * The actual BuildingDialog class
         */
        public Ep c;
        /**
         * Boolean indicating if building got cancelled and we should stop continuing
         */
        public boolean d = false;

        public a(Context context) {
            super(context);
            DesignActivity.this.a((MA) this);
            c = new Ep(DesignActivity.this);
            d();
            c.a(false);
        }

        /**
         * Reverts c (the "Run"-Button) to its original state,
         * usually called after compilation was successful.
         * <p>
         * This closes the dialog, reverts u (the "Run"-Button)'s text and clickable property,
         * and clears the FLAG_KEEP_SCREEN_ON flag.
         */
        @Override
        public void a() {
            q.b();
            c();
            runProject.setText(xB.b().a(getApplicationContext(), Resources.string.common_word_run));
            runProject.setClickable(true);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        /**
         * Shows a Toast about APK build having failed, closes the dialog,
         * reverts u (the "Run"-Button) and clears the FLAG_KEEP_SCREEN_ON flag.
         *
         * @param str Ignored parameter, for some reason
         */
        @Override
        public void a(String str) {
            runOnUiThread(() -> {
                q.b();
                c();
                SketchwareUtil.toastError("APK build failed");
                runProject.setText(xB.b().a(getApplicationContext(), Resources.string.common_word_run));
                runProject.setClickable(true);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            });
        }

        public void aWithMessage(String message) {
            runOnUiThread(() -> {
                a();
                SketchwareUtil.toastError("APK build failed: " + message, Toast.LENGTH_LONG);
            });
        }

        @Override
        protected void onProgressUpdate(String... values) {
            c(values[0]);
        }

        @Override
        public void b() {
            if (d) {
                cancel(true);
            } else {
                BuildSettings buildSettings = new BuildSettings(q.b);

                try {
                    publishProgress("Deleting temporary files...");
                    FileUtil.deleteFile(q.c);

                    q.c();
                    q.c(a);
                    q.a();
                    /* Extract project type template */
                    q.a(a, wq.e("600"));
                    if (yB.a(lC.b(sc_id), "custom_icon")) {
                        q.a(wq.e()
                                + File.separator + sc_id
                                + File.separator + "icon.png");
                    }

                    kC kC = jC.d(sc_id);
                    kC.b(q.w + File.separator + "drawable-xhdpi");
                    kC = jC.d(sc_id);
                    kC.c(q.w + File.separator + "raw");
                    kC = jC.d(sc_id);
                    kC.a(q.A + File.separator + "fonts");
                    n();
                    q.f();
                    q.e();

                    Dp mDp = new Dp(this, a, q);

                    publishProgress("Extracting AAPT/AAPT2 binaries...");
                    mDp.i();
                    if (d) {
                        cancel(true);
                        return;
                    }

                    publishProgress("Extracting built-in libraries...");
                    mDp.j();
                    if (d) {
                        cancel(true);
                        return;
                    }

                    publishProgress("AAPT2 is running...");
                    mDp.a();
                    if (d) {
                        cancel(true);
                        return;
                    }

                    publishProgress("Java is compiling...");

                    boolean incrementalCompilationEnabled = buildSettings.
                            getValue(BuildSettings.SETTING_INCREMENTAL_BUILD_ACTIVE, BuildSettings.SETTING_GENERIC_VALUE_FALSE)
                            .equals(BuildSettings.SETTING_GENERIC_VALUE_TRUE);

                    if (incrementalCompilationEnabled) {
                        IncrementalCompiler incrementalCompiler = new IncrementalCompiler(q);
                        incrementalCompiler.setResultListener((success, compileType, args) -> {
                            if (!success) {
                                DesignActivity.this.d(String.valueOf(args[0]));
                            } else {

                                switch (compileType) {
                                    case Compiler.TYPE_JAVA:
                                        publishProgress("Running D8...");
                                        break;

                                    case Compiler.TYPE_D8:
                                        publishProgress("Merging dex...");
                                        break;

                                    case Compiler.TYPE_MERGE:
                                        publishProgress("Building apk...");
                                        break;
                                }

                                publishProgress("Signing apk...");
                                mDp.k();

                                o();
                            }
                        });
                        incrementalCompiler.performCompilation();
                    } else {
                        /* Compile the regular, old way */
                        /* Compile Java classes */
                        mDp.f();
                        if (d) {
                            cancel(true);
                            return;
                        }

                        /* Encrypt Strings in classes if enabled */
                        StringfogHandler stringfogHandler = new StringfogHandler(q.b);
                        stringfogHandler.start(this, mDp);
                        if (d) {
                            cancel(true);
                            return;
                        }

                        /* Obfuscate classes if enabled */
                        ProguardHandler proguardHandler = new ProguardHandler(q.b);
                        proguardHandler.start(this, mDp);
                        if (d) {
                            cancel(true);
                            return;
                        }

                        /* Create DEX file(s) */
                        publishProgress(mDp.getDxRunningText());
                        mDp.c();
                        if (d) {
                            cancel(true);
                            return;
                        }

                        /* Merge DEX file(s) with libraries' dexes */
                        publishProgress("Merging libraries' DEX files...");
                        mDp.h();
                        if (d) {
                            cancel(true);
                            return;
                        }

                        publishProgress("Building APK...");
                        mDp.g();
                        if (d) {
                            cancel(true);
                            return;
                        }

                        publishProgress("Aligning APK...");
                        mDp.runZipalign();
                        if (d) {
                            cancel(true);
                            return;
                        }

                        publishProgress("Signing APK...");
                        if (Build.VERSION.SDK_INT >= 26) {
                            ApkSigner signer = new ApkSigner(a);
                            signer.signWithTestKey(mDp.f.alignedApkPath, mDp.f.H, null);
                        } else {
                            mDp.k();
                        }
                        if (d) {
                            cancel(true);
                            return;
                        }

                        /* Launch Intent to install APK */
                        o();
                    }
                } catch (Throwable e) {
                    LogUtil.e("DesignActivity$a", "Failed to build project", e);
                    DesignActivity.this.d(e.getMessage());
                }
            }
        }

        /**
         * Dismiss this dialog, if DesignActivity hasn't been destroyed.
         */
        public void c() {
            if (!isDestroyed()) {
                if (c.isShowing()) {
                    c.dismiss();
                }
            }
        }

        /**
         * Updates the dialog's progress text.
         *
         * @param progressText The new text to display as progress
         */
        public void c(String progressText) {
            runOnUiThread(new DesignActRunnable(c, progressText));
        }

        /**
         * Try to set this dialog's OnCancelListener as this, then show, unless already showing.
         */
        public void d() {
            if (!c.isShowing()) {
                c.setOnCancelListener(this);
                c.show();
            }
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            if (!c.a()) {
                c.a(true);
                d();
                publishProgress("Canceling build...");
                d = true;
            }
        }

        @Override
        public void onCancelled() {
            super.onCancelled();
            runOnUiThread(() -> {
                runProject.setText(xB.b().a(getApplicationContext(), Resources.string.common_word_run));
                runProject.setClickable(true);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            });
            q.b();
            c();
        }

        @Override
        protected String doInBackground(Void... voids) {
            return a(voids);
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            runProject.setText("Building APK file...");
            runProject.setClickable(false);
            r.a("P1I10", true);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    public class b extends MA {

        public Bundle c;

        public b(Context context, Bundle bundle) {
            super(context);
            DesignActivity.this.a(this);
            c = bundle;
        }

        @Override
        public void a() {
            if (c != null) {
                projectFileSelector.a(c);
                if (c.getInt("file_selector_current_file_type") == 0) {
                    xmlLayoutOrientation.setVisibility(View.VISIBLE);
                } else {
                    xmlLayoutOrientation.setVisibility(View.GONE);
                }
            }

            projectFileSelector.a();
            h();
            if (c == null) {
                l();
            }
        }

        @Override
        public void a(String str) {
            h();
        }

        @Override
        public void b() {
            DesignActivity.this.a(c != null);
        }

        @Override
        protected String doInBackground(Void... voids) {
            return a(voids);
        }
    }

    /**
     * A project "saver" AsyncTask that doesn't actually save the project.
     * Gets executed when clicking "Exit" in the "Save project?" dialog.
     */
    public class c extends MA {

        public c(Context context) {
            super(context);
            DesignActivity.this.a(this);
        }

        @Override
        public void a() {
            h();
            finish();
        }

        @Override
        public void a(String str) {
            h();
            finish();
        }

        public void b() {
            publishProgress("Now processing..");
            jC.d(sc_id).v();
            jC.d(sc_id).w();
            jC.d(sc_id).u();
        }

        @Override
        protected String doInBackground(Void... voids) {
            return a(voids);
        }
    }

    /**
     * An AsyncTask saving the project. This doesn't finish the activity, unlike {@link DesignActivity.e}.
     */
    public class d extends MA {

        public d(Context context) {
            super(context);
            DesignActivity.this.a(this);
        }

        @Override
        public void a() {
            bB.a(a, xB.b().a(a, Resources.string.common_message_complete_save), 0).show();
            A();
            h();
            jC.d(sc_id).f();
            jC.d(sc_id).g();
            jC.d(sc_id).e();
        }

        @Override
        public void a(String str) {
            bB.b(a, xB.b().a(a, Resources.string.common_error_failed_to_save), 0).show();
            DesignActivity.this.h();
        }

        @Override
        public void b() {
            publishProgress("Now saving..");
            jC.d(sc_id).a();
            jC.b(sc_id).m();
            jC.a(sc_id).j();
            jC.d(sc_id).x();
            jC.c(sc_id).l();
        }

        @Override
        protected String doInBackground(Void... voids) {
            return a(voids);
        }
    }

    /**
     * AsyncTask that saves the project when exiting {@link DesignActivity} normally.
     */
    public class e extends MA {

        public e(Context context) {
            super(context);
            DesignActivity.this.a(this);
        }

        @Override
        public void a() {
            bB.a(a, xB.b().a(a, Resources.string.common_message_complete_save), 0).show();
            A();
            h();
            finish();
        }

        @Override
        public void a(String str) {
            bB.b(a, xB.b().a(a, Resources.string.common_error_failed_to_save), 0).show();
            h();
        }

        @Override
        public void b() {
            publishProgress("Now saving..");
            jC.d(sc_id).a();
            jC.b(sc_id).m();
            jC.a(sc_id).j();
            jC.d(sc_id).x();
            jC.c(sc_id).l();
            jC.d(sc_id).h();
        }

        @Override
        protected String doInBackground(Void... voids) {
            return a(voids);
        }
    }

    public class f extends MA {

        public f(Context context) {
            super(context);
            DesignActivity.this.a(this);
        }

        @Override
        public void a() {
        }

        @Override
        public void a(String var1) {
        }

        @Override
        public void b() {
            jC.a(sc_id).k();
        }

        @Override
        protected String doInBackground(Void... voids) {
            return a(voids);
        }

        @Override
        public void onCancelled() {
            super.onCancelled();
        }
    }

    public class g extends gg {

        public final int f = 3;
        public String[] g;
        public Context h;

        public g(Xf xf, Context context) {
            super(xf);
            h = context;
            g = new String[]{
                    xB.b().a(context, Resources.string.design_tab_title_view),
                    xB.b().a(context, Resources.string.design_tab_title_event),
                    xB.b().a(context, Resources.string.design_tab_title_component)};
        }

        public int a() {
            return 3;
        }

        @Override
        public CharSequence a(int var1) {
            return g[var1];
        }

        @Override
        public Object a(ViewGroup viewGroup, int i) {
            Fragment fragment = (Fragment) super.a(viewGroup, i);
            if (i == 0) {
                viewTabAdapter = (jr) fragment;
            } else if (i == 1) {
                eventTabAdapter = (rs) fragment;
            } else {
                componentTabAdapter = (br) fragment;
            }

            return fragment;
        }

        @Override
        public Fragment c(int i) {
            if (i == 0) {
                return new jr();
            } else {
                return i == 1 ? new rs() : new br();
            }
        }
    }
}
