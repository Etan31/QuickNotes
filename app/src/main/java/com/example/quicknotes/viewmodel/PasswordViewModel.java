package com.example.quicknotes.viewmodel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.quicknotes.password.PasswordEntity;
import com.example.quicknotes.database.AppDatabase;

import java.lang.ref.WeakReference;

public class PasswordViewModel extends ViewModel {
    private MutableLiveData<PasswordEntity> passwordLiveData = new MutableLiveData<>();

    public LiveData<PasswordEntity> getPasswordLiveData() {
        return passwordLiveData;
    }

    @SuppressLint("StaticFieldLeak")
    public void loadPassword(Context context) {
        new AsyncTask<Void, Void, PasswordEntity>() {
            // Your existing AsyncTask code...

            @Override
            protected PasswordEntity doInBackground(Void... voids) {
                return AppDatabase.getInstance(context.getApplicationContext()).passwordDao().getPassword();
            }

            @Override
            protected void onPostExecute(PasswordEntity savedPassword) {
                passwordLiveData.setValue(savedPassword);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private static class PasswordAsyncTask extends AsyncTask<Void, Void, PasswordEntity> {
        private WeakReference<PasswordViewModel> viewModelWeakReference;
        private WeakReference<Context> contextWeakReference;

        PasswordAsyncTask(PasswordViewModel viewModel, Context context) {
            this.viewModelWeakReference = new WeakReference<>(viewModel);
            this.contextWeakReference = new WeakReference<>(context);
        }

        @Override
        protected PasswordEntity doInBackground(Void... voids) {
            Context context = contextWeakReference.get();
            PasswordViewModel viewModel = viewModelWeakReference.get();

            if (context != null && viewModel != null) {
                return AppDatabase.getInstance(context.getApplicationContext()).passwordDao().getPassword();
            }

            return null;
        }

        @Override
        protected void onPostExecute(PasswordEntity savedPassword) {
            PasswordViewModel viewModel = viewModelWeakReference.get();

            if (viewModel != null) {
                viewModel.passwordLiveData.setValue(savedPassword);
            }
        }
    }
}



