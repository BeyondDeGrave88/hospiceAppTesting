# Запуск авто-тестов для приложения FMHAndroid

Данный проект содержит набор UI-тестов, написанных с использованием Espresso и Allure для формирования отчётов. Тесты проверяют функциональность мобильного приложения «Хоспис».

---

## Требования

- Android Studio (рекомендуется последняя стабильная версия)
- Эмулятор Android с API 29+ (или реальное устройство с включённой отладкой по USB)
- Java 11
- Установленный Allure Commandline (для просмотра отчёта):
  ```bash
  brew install allure   # для macOS
  # или скачайте с https://github.com/allure-framework/allure2/releases
  
## Установка приложения на устройство
1. Подключите эмулятор или устройство и убедитесь, что оно видно:
  ```bash
  adb devices
```

2. Установите debug-версию приложения:
  ```bash
./gradlew installDebug
```
## Запуск авто-тестов
**Важно:** тесты необходимо запускать **только через Gradle**, а не через интерфейс Android Studio. Иначе Allure-раннер может не сработать, и шаги не запишутся в отчёт.
```bash
./gradlew connectedAndroidTest
```

## Генерация и просмотр Allure-отчёта
1. Дождитесь окончания тестов и скопируйте результаты на компьютер:

```bash
adb pull /sdcard/googletest/test_outputfiles/allure-results ./allure-results
```
2. Сгенерируйте и откройте отчёт в браузере:
```bash
allure serve ./allure-results
```

Для очистки старых результатов перед новым запуском выполните:
```bash
adb shell rm -rf /sdcard/googletest/test_outputfiles/allure-results
rm -rf allure-results
```


