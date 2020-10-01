import React, { useState, useEffect } from "react";

function MeetingEditor(props) {
  const [existingEmployees, setExistingEmployees] = useState([]);
  const [existingDepartments, setExistingDepartments] = useState([]);
  const [existingDepartmentMembers, setExistingDepartmentMembers] = useState(
    []
  );

  function renderParticipantsTableData() {
    return existingEmployees.map((employee) => {
      return (
        <tr key={employee.id}>
          <td>{employee.name}</td>
          <td>{employee.age}</td>
          <td>{employee.department}</td>
          <td>удалить</td>
        </tr>
      );
    });
  }

  return (
    <form class="flexbox-vertical">
      <label class="flexbox-horizontal">
        <l1 class="bold-text">Тема</l1>
        <input type="text" />
      </label>

      <label class="flexbox-horizontal">
        <l1 class="bold-text">Время проведения</l1>
        <input type="text" />
      </label>

      <label class="flexbox-horizontal">
        <l1 class="bold-text">Подразделение</l1>
        <select id="dep-select">
          {existingDepartments.map((department) => {
            return <option key={department.id}> {department.name}</option>;
          })}
        </select>
      </label>

      <label class="flexbox-horizontal">
        <l1 class="bold-text">Ответственный</l1>
        <select id="org-select">
          {existingDepartmentMembers.map((member) => {
            return <option key={member.id}> {member.name}</option>;
          })}
        </select>
      </label>

      <div>
        <l1 id="title">Участники</l1>
        <table id="participants">
          <tbody>{renderParticipantsTableData()}</tbody>
        </table>
      </div>

      <label class="flexbox-horizontal">
        <l1 class="bold-text">Участник</l1>
        <select id="cat-select">
          <option value="none" selected disabled hidden>
            Выберете участника
          </option>

          {existingEmployees.map((employee) => {
            return <option key={employee.id}> {employee.name}</option>;
          })}
        </select>
        <button>Добавить</button>
      </label>

      <label>
        <button>Сохранить</button>
        <button>Не сохранять</button>
      </label>
    </form>
  );
}
export default MeetingEditor;
