import React, { useState, useEffect } from "react";
import DateRangePicker from "@wojtekmaj/react-daterange-picker";
import DatePicker from "react-date-picker";

function MyApp() {
  const [value, onChange] = useState([new Date(), new Date()]);

  return (
    <div>
      <DateRangePicker onChange={onChange} value={value} />
    </div>
  );
}

function MeetingsView(props) {
  const url = "http://127.0.0.1:8080";
  var selectedDepartmentId;
  var selectedEmployeeId;
  var inputTheme;

  const [receivedMeetings, setReceivedMeetings] = useState([]);
  const [receivedEmployees, setReceivedEmployees] = useState([]);
  const [receivedDepartments, setReceivedDepartments] = useState([]);

  const [dateRange, setDateRange] = useState([new Date(), new Date()]);

  useEffect(() => {
    getDepartmentsQuery();
    getMeetingsQuery();
    getEmployeesQuery();
  }, []);

  const getMeetingsQuery = (event) => {
    console.log("in meet querry");
    const requestOptions = {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    };
    fetch(url + "/meetings", requestOptions)
      .then((response) => response.json())
      .then((response) => {
        console.log("receivedMeetings: ");
        console.log(response);
        setReceivedMeetings(response);
      })
      .catch((err) => console.log("Error " + err));
  };

  const getDepartmentsQuery = (event) => {
    console.log("in dep querry");
    const requestOptions = {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    };
    fetch(url + "/departments", requestOptions)
      .then((response) => response.json())
      .then((response) => {
        console.log("receivedDeps: ");
        console.log(response);
        setReceivedDepartments(response);
      })
      .catch((err) => console.log("Error " + err));
  };

  const getEmployeesQuery = (event) => {
    console.log("in empl querry");
    const requestOptions = {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    };
    fetch(url + "/employees", requestOptions)
      .then((response) => response.json())
      .then((response) => {
        setReceivedEmployees(response);
        console.log("receivedEmpl: ");
        console.log(response);
      })
      .catch((err) => console.log("Error " + err));
  };

  const searchQuery = (event) => {
    event.preventDefault();
    inputTheme = document.getElementById("theme-input").value;
    selectedEmployeeId = getIdFromSelect("empl-select");
    selectedDepartmentId = getIdFromSelect("dep-select");

    var dateFrom = dateRange[0];
    var dateTo = dateRange[1];

    console.log("dateFROM: " + dateFrom);

    console.log(
      "params: " +
        selectedEmployeeId +
        " " +
        selectedDepartmentId +
        " " +
        inputTheme +
        " " +
        dateFrom +
        " " +
        dateTo
    );

    const requestOptions = {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    };

    console.log("formatted date: " + formatDate(dateFrom));

    const searchParams = new URLSearchParams();
    // if (inputTheme !== "") {
    //   searchParams.append("theme", inputTheme);
    // }
    // if (dateFrom !== null && dateTo !== null) {
    //   searchParams.append("dateFrom", formatDate(dateFrom));
    //   searchParams.append("dateTo", formatDate(dateTo));
    // }
    if (selectedDepartmentId !== null) {
      searchParams.append(
        "departmentId",
        Number.parseInt(selectedDepartmentId)
      );
    }
    if (selectedEmployeeId !== null) {
      searchParams.append("employeeId", Number.parseInt(selectedEmployeeId));
    }

    console.log("search url : " + searchParams.toString());

    fetch(url + "/search?" + searchParams.toString(), requestOptions)
      .then((response) => {
        //console.log(response.json());
        return response.json();
      })
      .then((response) => setReceivedMeetings(response))
      .catch((err) => console.log("Error " + err));
  };

  function fetchNameById(array, id) {
    for (let element of array) {
      if (element.id === id) {
        return element.name;
      }
    }
  }

  //   function changeDepartment(event) {
  //     const selectedIndex = event.target.options.selectedIndex;
  //     selectedDepartmentId = event.target.options[selectedIndex].getAttribute(
  //       "data-key"
  //     );
  //   }

  //   function changeEmployee(event) {
  //     const selectedIndex = event.target.options.selectedIndex;
  //     selectedEmployeeId = event.target.options[selectedIndex].getAttribute(
  //       "data-key"
  //     );
  //   }

  function getIdFromSelect(selectId) {
    let select = document.getElementById(selectId);
    const selectedIndex = select.selectedIndex;
    return select.options[selectedIndex].getAttribute("id-key");
  }

  function formatDate(date) {
    var d = new Date(date),
      month = "" + (d.getMonth() + 1),
      day = "" + d.getDate(),
      year = d.getFullYear(),
      hour = "" + d.getHours(),
      minute = "" + d.getMinutes();

    if (month.length < 2) month = "0" + month;
    if (day.length < 2) day = "0" + day;
    if (hour.length < 2) hour = "0" + hour;
    if (minute.length < 2) minute = "0" + minute;

    return [year, month, day].join("-") + " " + ["00", "00"].join(":");
  }

  function changeDateRange(event) {
    console.log("dateRange: " + event.target.value);
    setDateRange(event.target.value);
  }

  return (
    <form class="flexbox-vertical">
      <label class="flexbox-horizontal">
        <l1 class="bold-text">Тема</l1>
        <input id="theme-input" type="text" />
      </label>

      <label class="flexbox-horizontal">
        <l1 class="bold-text">Время проведения: </l1>
        <div>
          <DateRangePicker onChange={setDateRange} value={dateRange} />
        </div>
      </label>

      <label class="flexbox-horizontal">
        <l1 class="bold-text">Подразделение</l1>
        <select id="dep-select">
          <option value="none" selected>
            Подразделение
          </option>
          {receivedDepartments.map((department) => {
            return <option id-key={department.id}> {department.name}</option>;
          })}
        </select>
      </label>

      <label class="flexbox-horizontal">
        <l1 class="bold-text">С участием</l1>
        <select id="empl-select">
          <option value="none" selected>
            Сотрудник
          </option>
          {receivedEmployees.map((employee) => {
            return <option id-key={employee.id}> {employee.shortName}</option>;
          })}
        </select>
      </label>

      <label>
        <button onClick={searchQuery}>Применить фильтр</button>
        <button>Показать все</button>
      </label>

      <div>
        <table id="meetingTable">
          <tbody>
            {receivedMeetings.map((meeting) => {
              return (
                <tr key={meeting.id}>
                  <td>{meeting.time}</td>
                  <td onClick={() => props.DisplayMeeting(meeting.id)}>
                    {meeting.theme}
                  </td>
                  <td>{meeting.department.name}</td>
                  <td>{meeting.organizer.shortName}</td>
                  <td>{meeting.count}</td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>

      <button onClick={() => props.DisplayMeeting()}>Добавить новое</button>
    </form>
  );
}
export default MeetingsView;
