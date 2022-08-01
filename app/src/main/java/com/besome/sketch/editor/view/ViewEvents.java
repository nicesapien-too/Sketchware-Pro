package com.besome.sketch.editor.view;

import a.a.a.Qs;
import a.a.a.bB;
import a.a.a.ci;
import a.a.a.jC;
import a.a.a.jy;
import a.a.a.mB;
import a.a.a.oq;
import a.a.a.wB;
import a.a.a.xB;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.besome.sketch.beans.EventBean;
import com.besome.sketch.beans.ProjectFileBean;
import com.besome.sketch.beans.ViewBean;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class ViewEvents extends LinearLayout {
    public String a;
    public ProjectFileBean b;
    public ViewBean c;
    public ArrayList<EventBean> d;
    public RecyclerView e;
    public Qs f;

    public ViewEvents(Context context) {
        super(context);
        a(context);
    }

    public void setOnEventClickListener(Qs qs) {
        this.f = qs;
    }

    /* loaded from: classes.dex */
    class a extends RecyclerView.a<a> {

        /* loaded from: classes.dex */
        class a extends RecyclerView.v {
            public LinearLayout t;
            public ImageView u;
            public ImageView v;
            public TextView w;

            public a(View view) {
                super(view);
                this.t = (LinearLayout) view.findViewById(2131230931);
                this.u = (ImageView) view.findViewById(2131231151);
                this.v = (ImageView) view.findViewById(2131231198);
                this.w = (TextView) view.findViewById(2131232195);
                view.setOnClickListener(new jy(this, a.this));
            }
        }

        public a() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.a
        /* renamed from: a */
        public void b(a aVar, int i) {
            EventBean eventBean = (EventBean) ViewEvents.this.d.get(i);
            if (eventBean.isSelected) {
                aVar.v.setVisibility(8);
                mB.a(aVar.u, 1);
            } else {
                aVar.v.setVisibility(0);
                mB.a(aVar.u, 0);
            }
            aVar.u.setImageResource(oq.a(eventBean.eventName));
            aVar.w.setText(eventBean.eventName);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.a
        public a b(ViewGroup viewGroup, int i) {
            return new a(LayoutInflater.from(viewGroup.getContext()).inflate(2131427414, viewGroup, false));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.a
        public int a() {
            return ViewEvents.this.d.size();
        }
    }

    public ViewEvents(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        a(context);
    }

    public final void a(Context context) {
        wB.a(context, this, 2131427775);
        this.d = new ArrayList<>();
        this.e = (RecyclerView) findViewById(2131231449);
        this.e.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.b(0);
        this.e.setLayoutManager(linearLayoutManager);
        this.e.setAdapter(new a());
        this.e.setItemAnimator(new ci());
    }

    public void a(String str, ProjectFileBean projectFileBean, ViewBean viewBean) {
        boolean z;
        this.a = str;
        this.b = projectFileBean;
        this.c = viewBean;
        String[] c = oq.c(viewBean.getClassInfo());
        this.d.clear();
        if (c != null) {
            ArrayList<EventBean> g = jC.a(str).g(projectFileBean.getJavaName());
            for (String str2 : c) {
                Iterator<EventBean> it = g.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        z = false;
                        break;
                    }
                    EventBean next = it.next();
                    if (next.eventType == 1 && viewBean.id.equals(next.targetId) && str2.equals(next.eventName)) {
                        z = true;
                        break;
                    }
                }
                if (!str2.equals("onBindCustomView") || (!viewBean.customView.equals("") && !viewBean.customView.equals("none"))) {
                    EventBean eventBean = new EventBean(1, viewBean.type, viewBean.id, str2);
                    eventBean.isSelected = z;
                    this.d.add(eventBean);
                }
            }
        }
        this.e.getAdapter().c();
    }

    public final void a(int i) {
        EventBean eventBean = this.d.get(i);
        if (!eventBean.isSelected) {
            eventBean.isSelected = true;
            jC.a(this.a).a(this.b.getJavaName(), eventBean);
            this.e.getAdapter().c(i);
            bB.a(getContext(), xB.b().a(getContext(), 2131625331), 0).show();
        }
        Qs qs = this.f;
        if (qs != null) {
            qs.a(eventBean);
        }
    }
}
