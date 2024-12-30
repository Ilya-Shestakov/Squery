package com.example.squery;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    private ImageView splashIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Устанавливаем layout для SplashScreenActivity
        setContentView(R.layout.splash_screen);

        // Находим ImageView иконки из нашего layout
        splashIcon = findViewById(R.id.splash_icon);

        // Запускаем анимацию на экране
        startSplashAnimation();
    }

    private void startSplashAnimation() {
        // 1. Создаем анимацию изменения прозрачности (альфы)
        // от 0 (полностью прозрачная) до 1 (полностью непрозрачная)
        ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(splashIcon, "alpha", 1f, 0f);

        // 2. Создаем анимации масштабирования иконки
        ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(splashIcon, "scaleX", 0.5f, 1f);
        ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(splashIcon, "scaleY", 0.5f, 1f);

        // 3. Устанавливаем длительность анимации в миллисекундах (1 секунда)
        fadeAnim.setDuration(1500);
        scaleXAnim.setDuration(1500);
        scaleYAnim.setDuration(1500);

        // 4. Создаем AnimatorSet для запуска анимации
        AnimatorSet set = new AnimatorSet();
        set.playTogether(fadeAnim, scaleXAnim, scaleYAnim);


        // 5. Устанавливаем интерполятор для анимации.
        // AccelerateDecelerateInterpolator создает эффект ускорения и замедления,
        // делая анимацию более плавной и естественной
        set.setInterpolator(new AccelerateDecelerateInterpolator());


        // 6. Добавляем слушатель анимации для отслеживания ее завершения
        set.addListener(new Animator.AnimatorListener() {

            // Этот метод вызывается, когда анимация только начинается (нам он не нужен)
            @Override
            public void onAnimationStart(Animator animation) {
                // Пустой, т.к. ничего не делаем в начале
            }

            // Этот метод вызывается, когда анимация заканчивается
            @Override
            public void onAnimationEnd(Animator animation) {
                // Как только анимация завершилась, мы переходим на основной экран
                navigateToMainActivity();
            }

            // Эти методы нам не нужны в данном примере, поэтому оставляем пустыми
            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        // 7. Запускаем анимацию!
        set.start();
    }

    private void navigateToMainActivity(){

        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }
}