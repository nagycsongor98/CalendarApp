const AVAILABLE_WEEK_DAYS = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
const localStorageName = 'calendar-events';
const ref = firebase.database().ref("Users");

var event;
var eventfromDB=new Array(0);


class EVENT{
    constructor(name, date, place, description){
        this.description = description;
        this.date = date;
        this.place = place;
        this.name = name;
        
    }
}

class CALENDAR {
    constructor(options) {
        this.options = options;
        this.elements = {
            days: this.getFirstElementInsideIdByClassName('calendar-days'),
            week: this.getFirstElementInsideIdByClassName('calendar-week'),
            month: this.getFirstElementInsideIdByClassName('calendar-month'),
            year: this.getFirstElementInsideIdByClassName('calendar-current-year'),
            eventList: this.getFirstElementInsideIdByClassName('current-day-events-list'),
            eventField: this.getFirstElementInsideIdByClassName('add-event-day-field'),
            eventAddBtn: document.getElementsByClassName('btn teal accent-4 add_event_button')[0],
            currentDay: this.getFirstElementInsideIdByClassName('calendar-left-side-day'),
            currentWeekDay: this.getFirstElementInsideIdByClassName('calendar-left-side-day-of-week'),
            prevYear: this.getFirstElementInsideIdByClassName('calendar-change-year-slider-prev'),
            nextYear: this.getFirstElementInsideIdByClassName('calendar-change-year-slider-next')
        };

        this.eventList = JSON.parse(localStorage.getItem(localStorageName)) || {};

        console.log(this.eventList);
        
        this.date = +new Date();
        this.options.maxDays = 37;

        ref.once('value',this.gotEvents);

        console.log(eventfromDB);

        this.populateEvents();

        this.drawEvents();
        this.init();
    }

    

// App methods
    init() {
        if (!this.options.id) return false;
        this.eventsTrigger();
        this.drawAll();
    }

    // draw Methods
    drawAll() {
        this.drawWeekDays();
        this.drawMonths();
        this.drawDays();
        this.drawYearAndCurrentDay();
        this.drawEvents();

        console.log(this.eventList);

    }

    drawEvents() {
        let calendar = this.getCalendar();
        let eventList = this.eventList[calendar.active.formatted] || ['You have no event today'];

        let eventTemplate = "";
        eventList.forEach(item => {
                if(item!='You have no event today'){
                    eventTemplate += `<li>${"Name:" + item.name + "<br />" + "Place:" + item.place + "<br />"+ "Description:" + item.description}</li>`;
                }
                else {
                    eventTemplate+= `<li>${item}</li>`
                }
        });
    
        this.elements.eventList.innerHTML = eventTemplate;
    }

    drawYearAndCurrentDay() {
        let calendar = this.getCalendar();
        this.elements.year.innerHTML = calendar.active.year;
        this.elements.currentDay.innerHTML = calendar.active.day;
        this.elements.currentWeekDay.innerHTML = AVAILABLE_WEEK_DAYS[calendar.active.week];
    }

    drawDays() {
        let calendar = this.getCalendar();

        let latestDaysInPrevMonth = this.range(calendar.active.startWeek).map((day, idx) => {
            return {
                dayNumber: this.countOfDaysInMonth(calendar.pMonth) - idx,
                month: new Date(calendar.pMonth).getMonth(),
                year: new Date(calendar.pMonth).getFullYear(),
                currentMonth: false
            }
        }).reverse();


        let daysInActiveMonth = this.range(calendar.active.days).map((day, idx) => {
            let dayNumber = idx + 1;
            let today = new Date();
            return {
                dayNumber,
                today: today.getDate() === dayNumber && today.getFullYear() === calendar.active.year && today.getMonth() === calendar.active.month,
                month: calendar.active.month,
                year: calendar.active.year,
                selected: calendar.active.day === dayNumber,
                currentMonth: true
            }
        });


        let countOfDays = this.options.maxDays - (latestDaysInPrevMonth.length + daysInActiveMonth.length);
        let daysInNextMonth = this.range(countOfDays).map((day, idx) => {
            return {
                dayNumber: idx + 1,
                month: new Date(calendar.nMonth).getMonth(),
                year: new Date(calendar.nMonth).getFullYear(),
                currentMonth: false
            }
        });

        let days = [...latestDaysInPrevMonth, ...daysInActiveMonth, ...daysInNextMonth];

        days = days.map(day => {
            let newDayParams = day;
            let formatted = this.getFormattedDate(new Date(`${Number(day.month) + 1}/${day.dayNumber}/${day.year}`));
            newDayParams.hasEvent = this.eventList[formatted];
            return newDayParams;
        });

        let daysTemplate = "";
        days.forEach(day => {
            daysTemplate += `<li class="${day.currentMonth ? '' : 'another-month'}${day.today ? ' active-day ' : ''}${day.selected ? 'selected-day' : ''}${day.hasEvent ? ' event-day' : ''}" data-day="${day.dayNumber}" data-month="${day.month}" data-year="${day.year}"></li>`
        });

        this.elements.days.innerHTML = daysTemplate;
    }

    drawMonths() {
        let availableMonths = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
        let monthTemplate = "";
        let calendar = this.getCalendar();
        availableMonths.forEach((month, idx) => {
            monthTemplate += `<li class="${idx === calendar.active.month ? 'active' : ''}" data-month="${idx}">${month}</li>`
        });

        this.elements.month.innerHTML = monthTemplate;
    }

    drawWeekDays() {
        let weekTemplate = "";
        AVAILABLE_WEEK_DAYS.forEach(week => {
            weekTemplate += `<li>${week.slice(0, 3)}</li>`
        });

        this.elements.week.innerHTML = weekTemplate;
    }

    pushEvent(data){
        //in users are the whole data
        var users = data.val();
        //keys are used to reffer to a specific user
        var keys = Object.keys(users);    

        for(var i=0; i<keys.length;i++)
        {
            if(users[keys[i]].email == localStorage.getItem("email")){
                ref.child(keys[i]).child("events").push(event);
                
            }
        }
    
    }

    gotEvents(data){
        var users = data.val();

        var keys = Object.keys(users);

        var _this =this;
        var array=[];

        for(var i=0; i<keys.length;i++)
        {
            if(users[keys[i]].email == localStorage.getItem("email")){
                var eventRefference = ref.child(keys[i]+"/events")

                eventRefference.on("value",function(dataSnapshot){
                    dataSnapshot.forEach(function(child) {
                        var singleEvent = child.val();   
                        array.push(singleEvent);
                        console.log(array);
                        console.log(array.length);
                        //console.log(_this.eventList);
                        
                        /*eventfromDB.push({
                            name:singleEvent.name,
                            date:singleEvent.date,
                            place:singleEvent.place,
                            description:singleEvent.description
                        });*/                
                      });
                });             
            }
        }
        console.log(array.length);
        
    }

    populateEvents(){

        console.log(eventfromDB);
        console.log(eventfromDB.length);

        for(var i=0;i<eventfromDB.length;i++)
        {
            console.log(eventfromDB[i].date);
            this.eventList[eventfromDB[i].date].push(eventfromDB[i]);
            localStorage.setItem(localStorageName, JSON.stringify(this.eventList));
        }
    }

    // Service methods
    eventsTrigger() {
        this.elements.prevYear.addEventListener('click', () => {
            let calendar = this.getCalendar();
            this.updateTime(calendar.pYear);
            this.drawAll()
        });

        this.elements.nextYear.addEventListener('click', () => {
            let calendar = this.getCalendar();
            this.updateTime(calendar.nYear);
            this.drawAll()
        });

        this.elements.month.addEventListener('click', e => {
            let calendar = this.getCalendar();
            let month = e.srcElement.getAttribute('data-month');
            if (!month || calendar.active.month == month) return false;

            let newMonth = new Date(calendar.active.tm).setMonth(month);
            this.updateTime(newMonth);
            this.drawAll()
        });


        this.elements.days.addEventListener('click', e => {
            console.log("bejon");
            console.log(this.eventList)
            let element = e.srcElement;
            let day = element.getAttribute('data-day');
            let month = element.getAttribute('data-month');
            let year = element.getAttribute('data-year');
            if (!day) return false;
            let strDate = `${Number(month) + 1}/${day}/${year}`;
            this.updateTime(strDate);
            this.drawAll()
        });

        this.elements.eventAddBtn.addEventListener('click', () => {
            var name = document.getElementById("event_name_id").value;
            var date = document.getElementById("event_date_id").value;
            var place = document.getElementById("event_place_id").value;
            var description = document.getElementById("event_description_id").value;
            event = new EVENT(name,date,place,description);

            event.date = this.getFormattedDate(new Date(event.date));

            if (!this.eventList[event.date]) 
                this.eventList[event.date] = [];

            this.eventList[event.date].push(event);
            localStorage.setItem(localStorageName, JSON.stringify(this.eventList));
            console.log(localStorage);
        
            ref.once('value',this.pushEvent);

            this.drawAll()
        });


    }


    updateTime(time) {
        this.date = +new Date(time);
    }

    getCalendar() {
        let time = new Date(this.date);

        return {
            active: {
                days: this.countOfDaysInMonth(time),
                startWeek: this.getStartedDayOfWeekByTime(time),
                day: time.getDate(),
                week: time.getDay(),
                month: time.getMonth(),
                year: time.getFullYear(),
                formatted: this.getFormattedDate(time),
                tm: +time
            },
            pMonth: new Date(time.getFullYear(), time.getMonth() - 1, 1),
            nMonth: new Date(time.getFullYear(), time.getMonth() + 1, 1),
            pYear: new Date(new Date(time).getFullYear() - 1, 0, 1),
            nYear: new Date(new Date(time).getFullYear() + 1, 0, 1)
        }
    }

    countOfDaysInMonth(time) {
        let date = this.getMonthAndYear(time);
        return new Date(date.year, date.month + 1, 0).getDate();
    }

    getStartedDayOfWeekByTime(time) {
        let date = this.getMonthAndYear(time);
        return new Date(date.year, date.month, 1).getDay();
    }

    getMonthAndYear(time) {
        let date = new Date(time);
        return {
            year: date.getFullYear(),
            month: date.getMonth()
        }
    }

    getFormattedDate(date) {
        return `${date.getDate()}/${date.getMonth()}/${date.getFullYear()}`;
    }

    range(number) {
        return new Array(number).fill().map((e, i) => i);
    }

    getFirstElementInsideIdByClassName(className) {
        return document.getElementById(this.options.id).getElementsByClassName(className)[0];
    }

    
}


window.addEventListener('load', function() {
    
    var elems = document.querySelectorAll('.datepicker');
    var instances = M.Datepicker.init(elems,{
        format:'m/d/yyyy'
    });

  });

(function () {

    email= localStorage.getItem("email");
    password= localStorage.getItem("password");
    
    localStorage.clear();
    localStorage.setItem("email",email);
    localStorage.setItem("password",password);
    new CALENDAR({
        id: "calendar",
    })

    
})();
