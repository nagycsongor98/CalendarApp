using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using Calendar.NET;

namespace Calendar.NETDemo
{
    public partial class Form1 : Form
    {
        [CustomRecurringFunction("RehabDates", "Calculates which days I should be getting Rehab")]

        public Form1()
        {
            InitializeComponent();

            calendar1.CalendarDate = new DateTime(2012, 5, 2, 0, 0, 0);
            calendar1.CalendarView = CalendarViews.Month;
            calendar1.AllowEditingEvents = true;

            
        }
    }
}
