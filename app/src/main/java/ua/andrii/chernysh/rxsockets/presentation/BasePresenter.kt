package ua.andrii.chernysh.rxsockets.presentation

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference

abstract class BasePresenter<M, V> {
     var model: M? = null
        set(value) {
            resetState()
            field = value
            if (setupDone()) {
                updateView()
            }
        }

    private var view: WeakReference<V>? = null
    private var mCompositeDisposable: CompositeDisposable? = CompositeDisposable()

    protected fun resetState() {}

    fun bindView(view: V) {
        this.view = WeakReference(view)
        if (setupDone()) {
            updateView()
        }
    }

    fun unbindView() {
        this.view = null

        disposeAll()
    }

    fun disposeAll() {

        if (mCompositeDisposable != null && !mCompositeDisposable!!.isDisposed) {
            mCompositeDisposable!!.dispose()
            mCompositeDisposable!!.clear()
            mCompositeDisposable = CompositeDisposable()
        }
    }

    protected fun addDisposable(disposable: Disposable) {
        if (mCompositeDisposable != null && !mCompositeDisposable!!.isDisposed) {
            mCompositeDisposable!!.add(disposable)
        }
    }

    protected fun view(): V? {
        return if (view == null) {
            null
        } else {
            view!!.get()
        }
    }

    protected abstract fun updateView()

    protected fun setupDone(): Boolean {
        return view() != null && model != null
    }

    abstract fun destroy()
}
