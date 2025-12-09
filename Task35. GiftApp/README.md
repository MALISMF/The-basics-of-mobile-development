# Gift App

Адаптивное Android приложение с подарком.

## Описание

Приложение демонстрирует адаптивный UI, который изменяется в зависимости от ориентации экрана:
- **Портретная ориентация**: элементы расположены вертикально
- **Ландшафтная ориентация**: изображение подарка слева, текст и кнопка справа

## Функциональность

- Открытие и закрытие подарка по нажатию кнопки
- Адаптивный интерфейс для разных ориентаций экрана
- Красивое векторное изображение подарка

## Технологии

- **Kotlin** 1.9.20
- **Android Gradle Plugin** 8.2.0
- **Gradle** 8.2
- **Min SDK** 24
- **Target SDK** 34
- **Compile SDK** 34

## Как открыть проект

1. Откройте Android Studio
2. Выберите `File` -> `Open`
3. Выберите папку `GiftApp`
4. Дождитесь синхронизации Gradle
5. Запустите приложение на эмуляторе или устройстве

## Структура проекта

```
GiftApp/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/giftapp/
│   │   │   │   └── MainActivity.kt
│   │   │   ├── res/
│   │   │   │   ├── layout/
│   │   │   │   │   └── activity_main.xml
│   │   │   │   ├── layout-land/
│   │   │   │   │   └── activity_main.xml
│   │   │   │   ├── drawable/
│   │   │   │   │   ├── gift_box.xml
│   │   │   │   │   └── gift_box_open.xml
│   │   │   │   └── values/
│   │   │   │       ├── strings.xml
│   │   │   │       ├── colors.xml
│   │   │   │       └── themes.xml
│   │   │   └── AndroidManifest.xml
│   │   └── test/
│   └── build.gradle
├── build.gradle
├── settings.gradle
└── gradle.properties
```

