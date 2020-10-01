import React, { useState, useEffect } from "react";
//import DateRangePicker from "@wojtekmaj/react-daterange-picker";
import DatePicker from "react-date-picker";

function MeetingsView(props) {
  const url = "http://127.0.0.1:8080";
  var selectedDepartmentId = "";
  var selectedEmployeeId = "";
  var inputTheme = "";
  var fromDate;
  var toDate;
  const [receivedMeetings, setReceivedMeetings] = useState([]);
  const [receivedEmployees, setReceivedEmployees] = useState([]);
  const [receivedDepartments, setReceivedDepartments] = useState([]);

  const [dateTo, setDateTo] = useState([new Date()]);
  const [dateFrom, setDateFrom] = useState([new Date()]);

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
      .then((response) => setReceivedMeetings(response))
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
      .then((response) => setReceivedDepartments(response))
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

    const searchParams = new URLSearchParams();
    if (inputTheme !== "") {
      searchParams.append("theme", inputTheme);
    }
    if (dateFrom !== null && dateTo !== null) {
      searchParams.append("dateFrom", formatDate(dateFrom));
      searchParams.append("dateTo", formatDate(dateTo));
    }
    if (selectedDepartmentId !== null) {
      searchParams.append("departmentId", selectedDepartmentId);
    }
    if (selectedDepartmentId !== null) {
      searchParams.append("employeeId", selectedEmployeeId);
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

  function renderMeetingsTableData(props) {
    return receivedMeetings.map((meeting, index) => {
      console.log(receivedEmployees);
      //console.log("depId " + receivedDepmeeting.departmentId);
      //console.log(Object.keys(meeting.participantIds).length);
      return (
        <tr key={meeting.id}>
          <td>{meeting.time}</td>
          <td onClick={() => props.DisplayMeeting(meeting.id)}>
            {meeting.theme}
          </td>
          <td>{fetchNameById(receivedDepartments, meeting.departmentId)}</td>
          <td>{fetchNameById(receivedEmployees, meeting.organizerId)}</td>
          <td>{Object.keys(meeting.participantIds).length}</td>
        </tr>
      );
    });
  }

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
    return select.options[selectedIndex].getAttribute("data-key");
  }

  function formatDate(date) {
    return (
      date.getDate() +
      "-" +
      parseInt(date.getMonth() + 1) +
      "-" +
      date.getFullYear()
    );
  }

  return (
    <form class="flexbox-vertical">
      <label class="flexbox-horizontal">
        <l1 class="bold-text">Тема</l1>
        <input id="theme-input" type="text" />
      </label>

      <label class="flexbox-horizontal">
        <l1 class="bold-text">Время проведения с</l1>
        <div>
          <DatePicker
            id="dateFrom-id"
            value={dateFrom}
            onChange={setDateFrom}
          />
          <l1 class="bold-text"> по </l1>
          <DatePicker id="dateTo-id" value={dateTo} onChange={setDateTo} />
        </div>
      </label>

      <label class="flexbox-horizontal">
        <l1 class="bold-text">Подразделение</l1>
        <select id="dep-select">
          <option value="none" selected disabled hidden>
            Подразделение
          </option>
          {receivedDepartments.map((department) => {
            return <option data-key={department.id}> {department.name}</option>;
          })}
        </select>
      </label>

      <label class="flexbox-horizontal">
        <l1 class="bold-text">С участием</l1>
        <select id="empl-select">
          <option value="none" selected disabled hidden>
            Сотрудник
          </option>
          {receivedEmployees.map((employee) => {
            return (
              <option data-key={employee.id}> {employee.shortName}</option>
            );
          })}
        </select>
      </label>

      <label>
        <button onClick={searchQuery}>Применить фильтр</button>
        <button>Показать все</button>
      </label>

      <div>
        <l1 id="title">Участники</l1>
        <table id="meetingTable">
          <tbody>{renderMeetingsTableData(props)}</tbody>
        </table>
      </div>
      <button>Добавить новое</button>
    </form>
  );
}
export default MeetingsView;
