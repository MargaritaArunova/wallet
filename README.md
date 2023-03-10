# **Кошелок**
> Приложение для распоряжения собственными финансами.

## Product Vision
______

#### *Бизнес идея проекта*
В современном мире в условиях неопределённости невозможно представить, что может случиться завтра. С каждым днём мы встречаем всё новые трудности и препятствия, которые нам нужно учится преодолевать. Так, совсем недавно многие пользователи мобильных и веб-приложений для финансов столкнулись с проблемами в учёте валютных транзакций. Для огромного количества людей валютный обмен, а также покупки в иностранной валюте перестали быть доступными из онлайн-сервисов. Это повлекло за собой увеличение количества случаев расчёта за покупки с помощью наличных, что усложнило ведение собственных финансов в связи с отсутствием статистик по транзакциям, которые предоставляют банки в своих приложениях.
Приложение "Кошелёк" призвано помочь пользователям вести удобный учёт финансов через добавление информации об операциях, которые они совершают в валюте.

#### *Целевая аудитория*
Пользователи наличного способа оплаты покупок.

#### *Объяснение успешности проекта*
На данный момент пользователи наличного способа оплаты покупок в большинстве случаев ведут дневник трат в заметках своего телефона или же на бумаге, что осложняет жизнь при подсчёте статистик, которые являются основным составляющим при ведении финансов. Также метод подсчёта статистик через приложение не допускает арифметических ошибок, которые может сделать человек.

#### *Объяснение возможного провала проекта*
Отсутствие людей, которые ведут учёт своих бюджетов. Нежелание скачивать на свои устройства объёмное по памяти приложение, которые можно заменить записями в блокноте и ручным подсчётом нужных статистик.

#### *Предоставляемый функционал*
Приложение предоставляет следующий функционал:
* возможность пользователю создать/удалять кошелёк
* возможность добавлять/удалять совершаемые операции (доход/расход)
* возможность указывать категорию операции
* возможность указывать лимиты на траты по тому или иному кошельку
* возможность просматривать текущий курс валют, а также курс валют за предыдущие дни

#### *Анализ рисков*
Были выделены следующие возможные риски при разработке приложения:
* функциональные:
    * проблемы с получением актуального курса валют и его отображением из ЦБ -- возможен отказ ЦБ в ответе на запрос в связи сильной нагрузки сервера
* нефункциональные:
    * отсутствие/прекращение доступа к технологиям, которые подразумевались для разработки проекта

## User Stories
______
Были выделены следующие сценарии:
* Как пользователь приложения я хочу создать кошелёк, чтобы в последствии хранить в нём информацию об операциях
* Как пользователь приложения я хочу удалить кошелёк, чтобы избавится от старой ненужной информации
* Как пользователь приложения я хочу создать операцию в кошельке, чтобы в последствии получать статистику о своих доходах/тратах
* Как пользователь приложения я хочу удалить операцию в кошельке, чтобы избавится от старой ненужной или ошибочной информации
* Как пользователь приложения я хочу указывать категорию доходов/расходов по операции, чтобы помнить информацию об этой операции в полном объёме
* Как пользователь приложения я хочу создавать свою категорию доходов/расходов, чтобы указывать её при выборе операции
* Как пользователь приложения я хочу указывать лимит на кошелёк, чтобы следить за своими тратами
* Как пользователь приложения я хочу получать информацию о валютах, чтобы знать актуальный обменный курс

![Usecase Diagram](/docs/usecase-diagram.png)


## Архитектура сервиса
______
#### *Общая архитектура сервиса*
![Architecture](/docs/architecture.png)


#### *Проектировка базы данных*
![Database Architecture](/docs/db-architecture.png)